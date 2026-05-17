package com.java.studentmanage.utils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

public class JwtUtil {

    private static final SecretKey KEY = Keys.hmacShaKeyFor(
            "StudentManageSecretKeyForJWT2024VeryLongEnough".getBytes(StandardCharsets.UTF_8));
    private static final long EXPIRATION = 86400000L;

    public static String generate(Long userId, String username) {
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION))
                .signWith(KEY)
                .compact();
    }

    public static Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(KEY)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public static Long getUserId(String token) {
        return parse(token).get("userId", Long.class);
    }

    public static String getUsername(String token) {
        return parse(token).getSubject();
    }
}
