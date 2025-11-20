package se.mojujo.userservice.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpHeaders;
import se.mojujo.userservice.user.CustomUser;
import se.mojujo.userservice.user.authority.UserRole;


import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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

    public String generateJwtToken(CustomUser customUser) { // TODO - CustomUserDetails
        logger.debug("Generating token for user: {} with roles {}", customUser.getUsername(), customUser.getRoles());

        List<String> roles = customUser.getRoles().stream().map(
                UserRole::getRoleName
        ).toList();

        String token = Jwts.builder()
                .subject(customUser.getUsername())
                .claim("authorities", roles)
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtExpirationMs))
                .signWith(key)
                .compact();

        logger.info("Successfully generated token for user: {}", customUser.getUsername());
        return token;
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

    public Set<UserRole> getRolesFromJwtToken(String token) {
        Claims claims = Jwts.parser()
                .verifyWith(key)
                .build()
                .parseSignedClaims(token)
                .getPayload();

        List<?> authoritiesClaim = claims.get("authorities", List.class);

        if (authoritiesClaim == null || authoritiesClaim.isEmpty()) {
            logger.warn("No authorities found in token");
            return Set.of();
        }

        Set<UserRole> roles = authoritiesClaim.stream()
                .filter(String.class::isInstance)
                .map(String.class::cast)
                .map(role -> role.replace("ROLE_", ""))
                .map(String::toUpperCase)
                .map(UserRole::valueOf)
                .collect(Collectors.toSet());

        logger.debug("Extracted roles: {}", roles);
        return roles;
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser()
                    .verifyWith(key)
                    .build()
                    .parseSignedClaims(authToken);

            logger.debug("JWT validation successful");
            return true;

        } catch (Exception e) {
            logger.error("JWT validation failed: {}", e.getMessage());
        }

        return false;
    }

    // Extract JWT from cookie
    String extractJwtFromCookie(HttpServletRequest request) {
        if (request.getCookies() == null) return null;

        for (Cookie c : request.getCookies()) {
            if ("authToken".equals(c.getName())) {
                return c.getValue();
            }
        }
        return null;
    }

    // Extract JWT from header
    String extractJwtFromRequest(HttpServletRequest request) {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (header != null && header.startsWith("Bearer ")) {
            return header.substring(7);
        }
        return null;
    }
}
