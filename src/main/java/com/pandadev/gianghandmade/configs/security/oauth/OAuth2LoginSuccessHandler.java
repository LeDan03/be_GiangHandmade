package com.pandadev.gianghandmade.configs.security.oauth;

import com.pandadev.gianghandmade.entities.User;
import com.pandadev.gianghandmade.exceptions.IllegalException;
import com.pandadev.gianghandmade.repositories.UserRepository;
import com.pandadev.gianghandmade.services.CookieService;
import com.pandadev.gianghandmade.utils.JWTUtil;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final UserRepository userRepository;
    private final JWTUtil jwtUtil;
    private final CookieService cookieService;

    @Value("${security.cookie.refresh.maxAge}")
    private long refreshMaxAge;

    @Value("${security.cookie.access.maxAge}")
    private long accessMaxAge;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        User user = userRepository.findByEmail(oAuth2User.getEmail())
                .orElseThrow(()->new IllegalException("Không tìm thấy người dùng sau oauth2 login"));
        String accessToken = jwtUtil.generateAccessToken(user.getEmail(), user.getId(), user.getRole());
        String refreshToken = jwtUtil.generateRefreshToken(user.getId());

        cookieService.addValueToCookie("refreshToken", refreshToken, refreshMaxAge, response);
        cookieService.addValueToCookie("accessToken", accessToken, accessMaxAge, response);

        response.sendRedirect("http://localhost:5173/");
    }
}
