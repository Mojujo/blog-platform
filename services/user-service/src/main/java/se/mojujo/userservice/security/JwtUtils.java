package se.mojujo.userservice.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import se.mojujo.userservice.user.CustomUser;
import se.mojujo.userservice.user.authority.UserRole;

import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class JwtUtils {

    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${jwt.secret}")
    private String base64Secret;
    private final SecretKey key = Keys.hmacShaKeyFor(Base64.getDecoder().decode(base64Secret));

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
}
