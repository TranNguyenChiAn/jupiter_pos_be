package com.jupiter.store.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    @Value("${jwt.secret}")
    private String secretKey;

    // Thời gian hết hạn token
    private long validityInMilliseconds = 3600000; // 1 hour

    private Key getSignInKey() {
        String base64Secret = Base64.getEncoder().encodeToString(secretKey.getBytes(StandardCharsets.UTF_8));
        byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    // Tạo JWT token từ username
    public String createToken(String username) {

        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + validityInMilliseconds))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Sử dụng SecretKey để ký
                .compact();
    }

    // Lấy thông tin (claims) từ token
    public Claims extractClaims(String token) {
        JwtParser jwtParser = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build();

        return jwtParser.parseClaimsJws(token).getBody();
    }

    // Lấy username từ token
    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    // Kiểm tra token có hết hạn hay không
    public boolean isTokenExpired(String token) {
        return extractClaims(token).getExpiration().before(new Date());
    }

    // Kiểm tra token có hợp lệ không
    public boolean validateToken(String token, String username) {
        return (username.equals(extractUsername(token)) && !isTokenExpired(token));
    }
}

