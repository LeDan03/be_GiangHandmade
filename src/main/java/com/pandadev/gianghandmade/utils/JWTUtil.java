package com.pandadev.gianghandmade.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {

    @Value("${security.jwt.access.secret-key}")
    private String accessSecretKey;

    @Value("${security.jwt.refresh.secret-key}")
    private String refreshSecretKey;

    @Value("${security.jwt.access.expiration-time}")
    private long accessExpirationTime;

    @Value("${security.jwt.refresh.expiration-time}")
    private long refreshExpirationTime;

    @Value("${security.jwt.issuer}")
    private String issuer;

    private SecretKey getAccessSecretKey() {
        return new SecretKeySpec(accessSecretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());
    }

    private SecretKey getRefreshSecretKey() {
        return new SecretKeySpec(refreshSecretKey.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS512.getJcaName());
    }

    public Claims extractAccessClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getAccessSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public Claims extractRefreshClaims(String token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getRefreshSecretKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
    }

    public String extractEmail(String token) {
        Claims claims = extractAccessClaims(token);
        return claims != null ? claims.getSubject() : null;
    }

    // Access Token
    public Long extractUserIdFromAccess(String token) {
        Claims claims = extractAccessClaims(token);
        return claims != null ? claims.get("userId", Long.class) : null;
    }

    // Refresh Token
    public Long extractUserIdFromRefresh(String token) {
        Claims claims = extractRefreshClaims(token);
        return claims != null ? Long.parseLong(claims.getSubject()) : null;
    }

    public boolean isTokenExpired(Claims claims) {
        return claims == null || claims.getExpiration().before(new Date());
    }

    public String extractRole(String token) {
        Claims claims = extractAccessClaims(token);
        return claims != null ? claims.get("role", String.class) : null;
    }

    public boolean validateAccessToken(String accessToken, String email) {
        Claims claims = extractAccessClaims(accessToken);
        return claims != null &&
                email.equals(claims.getSubject()) &&
                !isTokenExpired(claims) &&
                validateIssuer(claims);
    }

    public boolean validateRefreshToken(String refreshToken) {
        Claims claims = extractRefreshClaims(refreshToken);
        return claims != null &&
                validateIssuer(claims) &&
                !isTokenExpired(claims);
    }

    public boolean validateVerifyToken(String token) {
        Claims claims = extractAccessClaims(token);
        return claims != null &&
                !isTokenExpired(claims) &&
                extractUserIdFromSubject(token) != null &&
                validateIssuer(claims);
    }

    public boolean validateIssuer(Claims claims) {
        return issuer.equals(claims.getIssuer());
    }

    public String generateAccessToken(String email, long userId, String role) {
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(email)
                .claim("type", "access")
                .claim("userId", userId)
                .claim("role", role)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + accessExpirationTime))
                .signWith(getAccessSecretKey())
                .compact();
    }

    public String generateRefreshToken(long userId) {
        return Jwts.builder()
                .setIssuer(issuer)
                .setSubject(String.valueOf(userId))
                .claim("type", "refresh")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + refreshExpirationTime))
                .signWith(getRefreshSecretKey())
                .compact();
    }

    public String generateEmailVerificationToken(long id) {
        return Jwts.builder()
                .setSubject(String.valueOf(id))
                .setExpiration(new Date(System.currentTimeMillis() + 15 * 60 * 1000))
                .setIssuer(issuer)
                .signWith(getAccessSecretKey())
                .compact();
    }

    public String extractUserIdFromSubject(String token) {
        Claims claims = extractAccessClaims(token);
        return claims != null ? claims.getSubject() : null;
    }
}
