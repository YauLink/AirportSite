package com.myapp.Airports.service;

import com.myapp.Airports.model.JwtUserPrincipal;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;

@Service
public class JwtService {

    private final SecretKey signingKey;

    public JwtService(
            @Value("${jwt.secret}") String secret) {

        this.signingKey = Keys.hmacShaKeyFor(
                Decoders.BASE64.decode(secret)
        );
    }

    public Claims extractClaims(String token) {

        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public JwtUserPrincipal extractUser(String token) {

        Claims claims = extractClaims(token);

        Long userId = claims.get("userId", Long.class);
        String username = claims.getSubject();
        String fullName = claims.get("fullName", String.class);
        String role = claims.get("role", String.class);

        return new JwtUserPrincipal(
                userId,
                username,
                fullName,
                role
        );
    }

    public boolean isTokenValid(String token) {

        try {
            extractClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}