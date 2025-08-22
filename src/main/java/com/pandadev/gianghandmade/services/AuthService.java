package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.configs.security.oauth.CustomOAuth2User;
import com.pandadev.gianghandmade.dtos.UserEmailRoleDto;
import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.entities.enums.AuthProviders;
import com.pandadev.gianghandmade.entities.enums.Gender;
import com.pandadev.gianghandmade.exceptions.BadRequestException;
import com.pandadev.gianghandmade.exceptions.ForbiddenException;
import com.pandadev.gianghandmade.exceptions.NotFoundException;
import com.pandadev.gianghandmade.mappers.UserMapper;
import com.pandadev.gianghandmade.repositories.UserRepository;
import com.pandadev.gianghandmade.requests.LoginRequest;
import com.pandadev.gianghandmade.requests.RegisterRequest;
import com.pandadev.gianghandmade.responses.LoginResponse;
import com.pandadev.gianghandmade.responses.UserResponse;
import com.pandadev.gianghandmade.utils.JWTUtil;
import com.pandadev.gianghandmade.utils.UserValidationUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${security.cookie.refresh.maxAge}")
    private long refreshTokenExpiration;

    @Value("${security.cookie.access.maxAge}")
    private long accessTokenExpiration;

    @Value("${lgbt.user.default.avt}")
    private String otherAvatarUrl;
    @Value("${male.user.default.avt}")
    private String maleAvatarUrl;
    @Value("${female.user.default.avt}")
    private String femaleAvatarUrl;

    private final UserRepository userRepository;

    private final CookieService cookieService;
    private final CustomOAuth2UserService customOAuth2UserService;

    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserValidationUtil userValidationUtil;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserResponse emailRegistration(RegisterRequest registerRequest) {
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new BadRequestException("Email đã đăng ký tài khoản");
        }
        userValidationUtil.validateRegistration(registerRequest);
        Gender gender = Gender.valueOf(registerRequest.getGender().toUpperCase());
        AuthProviders authProviders = AuthProviders.valueOf(registerRequest.getAuthProviders().toUpperCase());
        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setUsername(registerRequest.getUsername());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setGender(gender);
        user.setAuthProviders(authProviders);
        user.setRole("USER");
        user.setAvatarUrl(getAvatarUrl(gender));
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }

    public UserResponse googleRegistrationAndLogin(OAuth2UserRequest request) {
        CustomOAuth2User user = (CustomOAuth2User) customOAuth2UserService.loadUser(request);
        return userMapper.toUserResponse(user);
    }

    public String getAvatarUrl(Gender gender) {
        return switch (gender) {
            case MALE -> maleAvatarUrl;
            case FEMALE -> femaleAvatarUrl;
            default -> otherAvatarUrl;
        };
    }

    public LoginResponse emailLogin(LoginRequest loginRequest, HttpServletRequest request,
                                    HttpServletResponse response, String oldRefreshToken) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword());
        Authentication authentication = authenticationManager.authenticate(token);
        User user = (User) authentication.getPrincipal();

        String refreshToken;
        if (oldRefreshToken == null || jwtUtil.isTokenExpired(jwtUtil.extractRefreshClaims(oldRefreshToken))) {
            refreshToken = jwtUtil.generateRefreshToken(user.getId());
            cookieService.updateValueFromCookie("refreshToken", refreshToken, refreshTokenExpiration, response);
        } else
            refreshToken = oldRefreshToken;
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId(), user.getRole());
        cookieService.addValueToCookie("accessToken", accessToken, accessTokenExpiration, response);

        return new LoginResponse(
                userMapper.toUserResponse(user),
                accessToken,
                refreshToken);
    }

    public String refreshAccessToken(String refreshToken) {
        if(!jwtUtil.validateRefreshToken(refreshToken)){
            throw new ForbiddenException("Refresh token không hợp lệ");
        }
        long id = jwtUtil.extractUserIdFromRefresh(refreshToken);
        Optional<UserEmailRoleDto> dtoOpt = userRepository.findUserEmailRoleById(id);
        if(!dtoOpt.isPresent()){
            throw new NotFoundException("Không tìm thấy người dùng này");
        }
        UserEmailRoleDto dto = dtoOpt.get();
        return jwtUtil.generateAccessToken(
                dto.getEmail(),
                id,
                dto.getRole()
        );
    }
}
