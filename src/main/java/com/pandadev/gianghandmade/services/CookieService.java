package com.pandadev.gianghandmade.services;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;

@Service
public class CookieService {

    @Value("${security.cookie.secure}")
    private boolean secure;

    public String getItemFromCookie(String key, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals(key)){
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

//Strict → Cookie chỉ được gửi khi request đến từ cùng domain.
//Lax → Cookie được gửi trong một số trường hợp an toàn (GET link, điều hướng) nhưng không gửi cho request cross-site kiểu fetch() hoặc XHR.
//None → Cookie luôn được gửi, kể cả cross-site request → nhưng bắt buộc phải đi kèm Secure (chỉ gửi qua HTTPS).
    public void addValueToCookie(String key, String value, long maxAge ,HttpServletResponse response){
        ResponseCookie responseCookie = ResponseCookie.from(key, value)
                .maxAge(maxAge)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public void deleteValueFromCookie(String key, HttpServletResponse response){
        ResponseCookie responseCookie = ResponseCookie.from(key, "")
                .maxAge(0)
                .httpOnly(true)
                .secure(secure)
                .path("/")
                .sameSite("Lax")
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, responseCookie.toString());
    }

    public void updateValueFromCookie(String key, String value,long maxAge ,HttpServletResponse response){
        addValueToCookie(key, value,maxAge ,response);
    }
}
