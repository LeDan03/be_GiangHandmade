package com.pandadev.gianghandmade.services;

import com.pandadev.gianghandmade.configs.security.oauth.CustomOAuth2User;
import com.pandadev.gianghandmade.dtos.UserEmailRoleDto;
import com.pandadev.gianghandmade.entities.Role;
import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.entities.enums.AuthProviders;
import com.pandadev.gianghandmade.entities.enums.Gender;
import com.pandadev.gianghandmade.entities.enums.UserStatus;
import com.pandadev.gianghandmade.exceptions.BadRequestException;
import com.pandadev.gianghandmade.exceptions.ConflicException;
import com.pandadev.gianghandmade.exceptions.ForbiddenException;
import com.pandadev.gianghandmade.exceptions.NotFoundException;
import com.pandadev.gianghandmade.mappers.UserMapper;
import com.pandadev.gianghandmade.repositories.RoleRepository;
import com.pandadev.gianghandmade.repositories.UserRepository;
import com.pandadev.gianghandmade.requests.LoginRequest;
import com.pandadev.gianghandmade.requests.RegisterRequest;
import com.pandadev.gianghandmade.responses.LoginResponse;
import com.pandadev.gianghandmade.responses.UserResponse;
import com.pandadev.gianghandmade.utils.JWTUtil;
import com.pandadev.gianghandmade.utils.UserValidationUtil;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.DependsOn;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
@DependsOn("roleService")
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
    @Value("${giang.user.default.avt}")
    private String giangAvatarUrl;

    @Value("${app.server.ip}")
    private String serverIp;

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    private final CookieService cookieService;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final MailService mailService;

    private final JWTUtil jwtUtil;
    private final UserMapper userMapper;
    private final UserValidationUtil userValidationUtil;
    private final AuthenticationManager authenticationManager;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostConstruct
    public void createAdmin(){
        if(userRepository.existsByIdNotNull()){
            return;
        }
        Optional<Role> adminRoleOpt = roleRepository.findByName("ADMIN");
        if(adminRoleOpt.isEmpty()){
            throw new NotFoundException("Admin Role Not Found");
        }
        Role adminRole = adminRoleOpt.get();
        User admin = new User();
        admin.setName("Hương Giang");
        admin.setGender(Gender.FEMALE);
        admin.setPassword(bCryptPasswordEncoder.encode("GiangYeuDan_ed"));
        admin.setRole(adminRole);
        admin.setAvatarUrl(giangAvatarUrl);
        admin.setStatus(UserStatus.MANAGER);
        admin.setEmail("ng.hgiang1012@gmail.com");
        userRepository.save(admin);
    }

    //Don user rac lúc 0h0p
    @Scheduled(cron = "0 0 0 * * ?")
    public void clearInactiveUsers() {
        userRepository.clearInactiveUsers();
    }

    public User createEmailUser(RegisterRequest registerRequest, UserStatus userStatus){
        log.info("Register request: {}", registerRequest);
        Optional<User> userInDbOpt = userRepository.findByEmail(registerRequest.getEmail());
        Optional<Role> roleOpt = roleRepository.findByName("USER");
        if(roleOpt.isEmpty()){
            throw new NotFoundException("Role USER không tìm thấy");
        }
        if (userInDbOpt.isPresent()) {
            User user = userInDbOpt.get();
            if(user.getStatus().equals(UserStatus.INACTIVE)){
                return user;
            }
            else throw new BadRequestException("Email đã đăng ký tài khoản");
        }
        Role role_user = roleOpt.get();
        userValidationUtil.validateRegistration(registerRequest);
        Gender gender = Gender.valueOf(registerRequest.getGender().toUpperCase());
        AuthProviders authProviders = AuthProviders.valueOf(registerRequest.getAuthProviders().toUpperCase());

        User user = new User();
        user.setEmail(registerRequest.getEmail());
        user.setName(registerRequest.getName());
        user.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        user.setGender(gender);
        user.setAuthProviders(authProviders);
        user.setRole(role_user);
        user.setAvatarUrl(getAvatarUrl(gender));
        user.setStatus(userStatus);
        userRepository.save(user);

        return user;
    }

    public void emailRegistration(RegisterRequest registerRequest) {
        if(userRepository.existsByEmail(registerRequest.getEmail())) {
            throw new ConflicException("Email đã đăng ký tài khoản rồi");
        }
        User user = createEmailUser(registerRequest, UserStatus.INACTIVE);

        String token = jwtUtil.generateEmailVerificationToken(user.getId());
        String verifyUrl = String.format("http://%s:8089/auth/users/email/verification?token=%s", serverIp ,token);

        mailService.sendVerificationEmail(registerRequest.getEmail(), verifyUrl);
    }
    public String verifyEmail(String token, Model model) {
        try{
            if(!jwtUtil.validateVerifyToken(token)){
                model.addAttribute("status", false);
                model.addAttribute("message", "Yêu cầu xác thực không hợp lệ hoặc đã hết thời gian xác thực");
                return "verification-email-result";
            }
            long userId = Long.parseLong(jwtUtil.extractUserIdFromSubject(token));
            userRepository.updateUserStatus(userId, UserStatus.ACTIVE);

            model.addAttribute("status", true);
            model.addAttribute("message", "Xác thực thành công, hãy đăng nhập ngay bạn nhé!");
            return  "verification-email-result";
        }catch (Exception e){
            model.addAttribute("status", false);
            model.addAttribute("message", "Lỗi xảy ra: "+e.getMessage());
            return "verification-email-result";
        }
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
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId(), user.getRole().getName());
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
        if(dtoOpt.isEmpty()){
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
