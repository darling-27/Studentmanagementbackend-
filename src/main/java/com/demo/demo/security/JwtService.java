package com.demo.demo.security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final Logger log = LoggerFactory.getLogger(JwtService.class);

    @Value("${app.jwt.secret}")
    private String secret;

    @Value("${app.jwt.expiration-ms}")
    private long expirationMs;

    @PostConstruct
    public void init() {
        if (secret == null || secret.isBlank()) {
            log.warn("JWT secret is not configured. Tokens cannot be generated or validated.");
            return;
        }
        try {
            base64Key();
            log.info("JWT secret loaded successfully (Base64 format)");
        } catch (IllegalArgumentException e) {
            log.warn("JWT secret is not valid Base64. Falling back to raw UTF-8 key. " +
                     "For production, use a Base64-encoded secret: openssl rand -base64 48");
        }
    }

    private SecretKey base64Key() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
    }

    private SecretKey rawUtf8Key() {
        return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateToken(UserDetails userDetails) {
        if (userDetails == null || userDetails.getUsername() == null) {
            throw new IllegalArgumentException("UserDetails and username must not be null");
        }

        Set<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        Date now = new Date();
        Date expiry = new Date(now.getTime() + expirationMs);

        return Jwts.builder()
                .subject(userDetails.getUsername())
                .claim("roles", roles)
                .issuedAt(now)
                .expiration(expiry)
                .signWith(base64Key(), Jwts.SIG.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        if (token == null || token.isBlank()) {
            return null;
        }
        try {
            return parse(token).getPayload().getSubject();
        } catch (JwtException | IllegalArgumentException ex) {
            log.debug("Invalid JWT token: {}", ex.getMessage());
            return null;
        }
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        if (username == null) {
            return false;
        }
        return username.equals(userDetails.getUsername()) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        Date expiration = parse(token).getPayload().getExpiration();
        return expiration != null && expiration.before(new Date());
    }

    private Jws<Claims> parse(String token) {
        try {
            return Jwts.parser().verifyWith(base64Key()).build().parseSignedClaims(token);
        } catch (SignatureException e) {
            log.debug("Primary key failed, trying fallback key for token");
            return Jwts.parser().verifyWith(rawUtf8Key()).build().parseSignedClaims(token);
        }
    }
}
