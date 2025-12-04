package se.mojujo.blogservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
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
import se.mojujo.blogservice.util.LogUtil;

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
        LogUtil.info(logger, "JWT_INIT", "JWT secret key initialized successfully");
    }

    private final int jwtExpirationMs = (int) TimeUnit.HOURS.toMillis(1);

    public String generateJwtToken(String username, UUID userId, Set<String> roles) {
        String token = Jwts.builder()
                .subject(username)
                .claim("userId", userId.toString())
                .claim("roles", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();

        LogUtil.info(logger,
                "JWT_GENERATED",
                "Token generated successfully",
                "username", username,
                "userId", userId.toString(),
                "roles", roles.toString());

        return token;
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token);

            LogUtil.info(logger, "JWT_VALIDATION_SUCCESS", "JWT validation successful");
            return true;

        } catch (Exception e) {
            LogUtil.error(logger, "JWT_VALIDATION_FAILED", e.getMessage());
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
            LogUtil.info(logger, "JWT_USERNAME_EXTRACTED", null, "username", username);
            return username;

        } catch (Exception e) {
            LogUtil.warn(logger, "JWT_USERNAME_FAILED", e.getMessage());
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

            LogUtil.info(logger, "JWT_USERID_EXTRACTED", null, "userId", id);

            return UUID.fromString(id);

        } catch (Exception e) {
            LogUtil.warn(logger, "JWT_USERID_FAILED", e.getMessage());
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    public Set<String> getRolesFromJwtToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();

            List<String> roles = claims.get("authorities", List.class);
            if (roles == null || roles.isEmpty()) {
                LogUtil.warn(logger, "JWT_NO_ROLES", "No roles found inside JWT");

                return Set.of();
            }

            Set<String> extractedRoles = new HashSet<>(roles);

            LogUtil.info(logger, "JWT_ROLES_EXTRACTED", null, "roles", extractedRoles.toString());

            return extractedRoles; // Roles extracted as Strings
        } catch (Exception e) {
            LogUtil.warn(logger, "JWT_ROLES_FAILED", e.getMessage(), "token", LogUtil.maskToken(token));
        }
        return Set.of();
    }

    // Extract JWT from cookie
    public String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie c : request.getCookies()) {
            if ("authToken".equals(c.getName())) {
                LogUtil.info(logger, "JWT_FROM_COOKIE", "JWT extracted from cookie");

                return c.getValue();
            }
        }
        return null;
    }

    // Extract JWT from header
    public String extractJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            LogUtil.info(logger, "JWT_FROM_HEADER", "JWT extracted from Authorization header");

            return header.substring(7);
        }
        return null;
    }
}
