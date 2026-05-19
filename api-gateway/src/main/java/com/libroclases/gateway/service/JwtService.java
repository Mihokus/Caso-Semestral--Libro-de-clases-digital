package com.libroclases.gateway.service;

import com.libroclases.gateway.model.Role;
import com.libroclases.gateway.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Service
public class JwtService {

    @Value("${jwt.secret}")
    private String jwtSecret;

    @Value("${jwt.expiration}")
    private long expirationMs;

    private SecretKey key() {
        return new SecretKeySpec(jwtSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
    }

    public String generate(User user) {
        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        List<String> roleNames = user.getRoles() == null
                ? List.of()
                : user.getRoles().stream().map(Role::getNombre).toList();

        return Jwts.builder()
                .subject(user.getEmail())
                .claim("userId", user.getId())
                .claim("nombre", user.getNombre())
                .claim("entityId", user.getEntityId())
                .claim("roles", roleNames)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(key(), Jwts.SIG.HS256)
                .compact();
    }

    public Claims parse(String token) {
        return Jwts.parser()
                .verifyWith(key())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    @SuppressWarnings("unchecked")
    public Set<String> rolesFrom(Claims claims) {
        Object raw = claims.get("roles");
        if (raw instanceof List<?> list) {
            return Set.copyOf(list.stream().map(Object::toString).toList());
        }
        return Set.of();
    }
}
