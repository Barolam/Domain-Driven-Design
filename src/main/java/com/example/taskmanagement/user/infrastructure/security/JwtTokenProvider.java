package com.example.taskmanagement.user.infrastructure.security;

import com.example.taskmanagement.user.application.port.TokenProviderPort;
import com.example.taskmanagement.user.domain.model.Permission;
import com.example.taskmanagement.user.domain.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider implements TokenProviderPort {

    private final Key key;
    private final long expiration;

    public JwtTokenProvider(
        @Value("${jwt.secret:9a4f2c8d3b7a1e6f45c8a0b3f2e1d4c7a8b9c0d1e2f3a4b5c6d7e8f9a0b1c2d3}") String secret,
        @Value("${jwt.expiration:86400000}") long expiration
    ) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiration = expiration;
    }

    @Override
    public String generateToken(User user) {
        List<String> permissions = user.getRole().getPermissions().stream()
            .map(Permission::getValue)
            .collect(Collectors.toList());

        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
            .setSubject(user.getId().toString())
            .claim("username", user.getUsername())
            .claim("role", user.getRole().name())
            .claim("permissions", permissions)
            .setIssuedAt(now)
            .setExpiration(expiryDate)
            .signWith(key)
            .compact();
    }

    @Override
    public UUID extractUserId(String token) {
        Claims claims = getClaims(token);
        return UUID.fromString(claims.getSubject());
    }

    public String extractUsername(String token) {
        Claims claims = getClaims(token);
        return claims.get("username", String.class);
    }

    public String extractRole(String token) {
        Claims claims = getClaims(token);
        return claims.get("role", String.class);
    }

    @SuppressWarnings("unchecked")
    public List<String> extractPermissions(String token) {
        Claims claims = getClaims(token);
        return claims.get("permissions", List.class);
    }

    @Override
    public boolean validateToken(String token) {
        try {
            getClaims(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    private Claims getClaims(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .getBody();
    }
}
