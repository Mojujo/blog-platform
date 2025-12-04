package se.mojujo.userservice.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;
import se.mojujo.userservice.exception.AuthorizationExpired;
import se.mojujo.userservice.exception.InvalidCredentialsException;
import se.mojujo.userservice.security.JwtUtils;
import se.mojujo.userservice.security.dto.AuthResponseDTO;
import se.mojujo.userservice.user.CustomUserDetails;
import se.mojujo.userservice.user.dto.CustomUserResponseDTO;
import se.mojujo.userservice.user.mapper.CustomUserMapper;
import se.mojujo.userservice.util.LogUtil;

import java.util.List;

@Service
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final CustomUserMapper customUserMapper;

    public AuthService(AuthenticationManager authenticationManager, JwtUtils jwtUtils, CustomUserMapper customUserMapper) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.customUserMapper = customUserMapper;
    }

    public AuthResponseDTO login(String username, String password) {

        LogUtil.info(logger, "LOGIN_ATTEMPT", null, "username", username);

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password)
            );

            CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();

            // Generate and return Token
            String token = jwtUtils.generateJwtToken(customUserDetails.getCustomUser());

            // List Roles
            List<String> roles = customUserDetails.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .toList();

            LogUtil.info(logger,
                    "LOGIN_SUCCESS",
                    null,
                    "username", username, "userId", customUserDetails.getCustomUser().getId());

            return new AuthResponseDTO(token, roles);

        } catch (AuthenticationException e) {
            LogUtil.warn(logger, "LOGIN_FAILED", null, "username", username);
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public void logout(HttpServletResponse response, HttpServletRequest request) {

        LogUtil.info(logger, "LOGOUT_ATTEMPT", null);

        Cookie authCookie = new Cookie("authToken", null);
        authCookie.setHttpOnly(true);
        authCookie.setSecure(false);
        authCookie.setPath("/");
        authCookie.setMaxAge(0);
        response.addCookie(authCookie);

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
            LogUtil.info(logger, "SESSION_INVALIDATED", null);
        }

        LogUtil.info(logger, "LOGOUT_SUCCESS", null);
    }

    public CustomUserResponseDTO getCurrentUser(Authentication authentication) {
        if (authentication == null || !authentication.isAuthenticated() ||
                !(authentication.getPrincipal() instanceof CustomUserDetails customUserDetails)) {
            throw new AuthorizationExpired("Authorization expired");
        }

        return customUserMapper.toResponseDTO(customUserDetails.getCustomUser());
    }
}
