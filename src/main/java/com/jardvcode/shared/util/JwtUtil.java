package com.jardvcode.shared.util;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

public class JwtUtil {

    private static final String SECRET_KEY = "your-secret-key-your-secret-key-123456";
    private static final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    
    public static String generateToken(String subject) {
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, subject);
    }
    
    private static String createToken(Map<String, Object> claims, String subject) {
        Instant now = Instant.now();
        Instant expirationTime = now.plus(1, ChronoUnit.HOURS);
        
        return Jwts.builder()
        		.claims(claims)
                .subject(subject)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expirationTime))
                .signWith(key, Jwts.SIG.HS256)
                .compact();
    }
    
    public static boolean validateToken(String token, String username) {
        try {
            String extractedUsername = extractUsername(token);
            return extractedUsername.equals(username) && !isTokenExpired(token);
        } catch (JwtException e) {
            return false;
        }
    }
    
    
    public static String extractUsername(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }
    
    private static boolean isTokenExpired(String token) {
        return extractExpiration(token).isBefore(Instant.now());
    }
    
    private static Instant extractExpiration(String token) {
        return Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getExpiration()
                .toInstant();
    }
    
}