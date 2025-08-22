package com.pandadev.gianghandmade.controllers;

import com.pandadev.gianghandmade.exceptions.UnauthorizedException;
import com.pandadev.gianghandmade.repositories.UserRepository;
import com.pandadev.gianghandmade.requests.LoginRequest;
import com.pandadev.gianghandmade.requests.RegisterRequest;
import com.pandadev.gianghandmade.responses.ApiResponse;
import com.pandadev.gianghandmade.responses.LoginResponse;
import com.pandadev.gianghandmade.responses.TokenResponse;
import com.pandadev.gianghandmade.responses.UserResponse;
import com.pandadev.gianghandmade.services.AuthService;
import com.pandadev.gianghandmade.services.CookieService;
import com.pandadev.gianghandmade.utils.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final CookieService cookieService;

    @Value("${security.jwt.access.expiration-time}")
    private long accessTokenExpirationTime;

    @PostMapping(value = "/login")
    @ResponseBody
    public ResponseEntity<LoginResponse> emailLogin(@RequestBody LoginRequest loginRequest,
                                                    HttpServletRequest request,
                                                    HttpServletResponse response,
                                                    @CookieValue(value = "refreshToken", required = false)  String oldRefreshToken) {
        return ResponseEntity.ok().body(authService.emailLogin(loginRequest, request, response, oldRefreshToken));
    }

    @PostMapping(value = "/registration/email")
    @ResponseBody
    public ResponseEntity<UserResponse> emailRegistration(@RequestBody RegisterRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.emailRegistration(request));
    }

    @PostMapping(value = "/accessToken/refresh")
    @ResponseBody
    public ResponseEntity<TokenResponse> refreshAccessToken(@CookieValue(value = "refreshToken") String refreshToken,
                                                            HttpServletResponse response) {
        if(refreshToken.isEmpty()){
            throw new UnauthorizedException("Không có refresh token, không thể cấp lại access token");
        }
        String newAccessToken = authService.refreshAccessToken(refreshToken);
        cookieService.addValueToCookie("accessToken", newAccessToken, accessTokenExpirationTime, response);
        return ResponseEntity.ok().body(new TokenResponse(newAccessToken));
    }

    @PostMapping(value = "/logout")
    @ResponseBody
    public ResponseEntity<ApiResponse> logout(HttpServletResponse response) {
        cookieService.deleteValueFromCookie("accessToken", response);
        cookieService.deleteValueFromCookie("refreshToken", response);
        return ResponseEntity.ok().body(new ApiResponse("Đã thoát phiên làm việc", 200));
    }
}
