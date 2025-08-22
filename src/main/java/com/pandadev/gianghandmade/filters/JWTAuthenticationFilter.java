package com.pandadev.gianghandmade.filters;

import com.pandadev.gianghandmade.configs.security.UserPrincipal;
import com.pandadev.gianghandmade.utils.JWTUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import java.io.IOException;
import java.util.*;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    private static final Set<String> PUBLIC_URLS = Set.of(
            "/auth/login",
            "/auth/registration/email",
            "/auth/accessToken/refresh",
            "/auth/verify",
            "/auth/logout"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        if (isPublic(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String accessToken = resolveToken(request);
        if (accessToken == null) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String email = jwtUtil.extractEmail(accessToken);
        if (email == null || !jwtUtil.validateAccessToken(accessToken, email)) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        String role = jwtUtil.extractRole(accessToken);
        long userId = jwtUtil.extractUserIdFromAccess(accessToken);

        UsernamePasswordAuthenticationToken auth = buildAuthenticationToken(email, role, userId);
        SecurityContextHolder.getContext().setAuthentication(auth);
        filterChain.doFilter(request, response);
    }

    private boolean isPublic(HttpServletRequest request) {
        return PUBLIC_URLS.stream().anyMatch(matcher -> matcher.matches(request.getRequestURI()));
    }

    private String resolveToken(HttpServletRequest httpServletRequest) {
        String bearer = httpServletRequest.getHeader("Authorization");
        if (bearer != null && bearer.startsWith("Bearer ")) {
            return bearer.substring(7);
        }
        Cookie cookie = WebUtils.getCookie(httpServletRequest, "accessToken");
        return cookie != null ? cookie.getValue() : null;
    }

    private static UsernamePasswordAuthenticationToken buildAuthenticationToken(String email, String role, long userId) {
        List<GrantedAuthority> grantedAuthorities =
                Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role));
        UserPrincipal userPrincipal = new UserPrincipal(userId, email);
        return new UsernamePasswordAuthenticationToken(userPrincipal, null, grantedAuthorities);
    }
}
