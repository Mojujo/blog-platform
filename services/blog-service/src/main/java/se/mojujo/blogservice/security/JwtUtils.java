package se.mojujo.blogservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${JWT_SECRET}")
    private String base64Secret;
    private SecretKey key;

    @PostConstruct
    public void init() {
        byte[] decodedKey = Base64.getDecoder().decode(base64Secret);
        this.key = Keys.hmacShaKeyFor(decodedKey);
    }

    private final int jwtExpirationMs = (int) TimeUnit.HOURS.toMillis(1);

    public String generateJwtToken(String username, UUID userId, Set<String> roles) {
        return Jwts.builder()
                .subject(username)
                .claim("userId", userId.toString())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            logger.debug("JWT validation successful");
            return true;

        } catch (Exception e) {
            logger.error("JWT validation failed: {}", e.getMessage());
        }

        return false;
    }

    public String getUsernameFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String username = claims.getSubject();
            logger.debug("Extracted username: {}", username);
            return username;

        } catch (Exception e) {
            logger.warn("Failed to extract username: {}", e.getMessage());
            return null;
        }
    }

    public UUID getUserIdFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            String id = claims.get("userId", String.class);

            logger.debug("Extracted user id: {}", id);

            return UUID.fromString(id);

        } catch (Exception e) {
            logger.warn("Failed to extract user id: {}", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Set<String> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<String> roles = claims.get("authorities", List.class);
        if (roles == null) return Set.of();
        return new HashSet<>(roles); // Roles extracted as Strings
    }

    // Extract JWT from cookie
    public String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie c : request.getCookies()) {
            if ("authToken".equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    // Extract JWT from header
    public String extractJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
