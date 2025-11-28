package se.mojujo.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se.mojujo.userservice.util.LogUtil;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils, UserDetailsService userDetailsService) {
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        LogUtil.info(logger, "JWT_FILTER_START", null, "requestURI", request.getRequestURI());

        // Extract token
        String token = jwtUtils.extractJwtFromCookie(request);
        if (token == null) {
            token = jwtUtils.extractJwtFromRequest(request); // Fallback to auth header
        }

        if (token == null) {
            LogUtil.info(logger, "JWT_TOKEN_MISSING", "No JWT token found in request", "requestURI", request.getRequestURI());
            filterChain.doFilter(request, response);
            return;
        }

        LogUtil.info(logger, "JWT_TOKEN_FOUND", "JWT token detected in request", "token", LogUtil.maskToken(token), "requestURI", request.getRequestURI());

        //Validate token
        if (jwtUtils.validateJwtToken(token)) {
            String username = jwtUtils.getUsernameFromJwtToken(token);

            // Ensures user still exists
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username); // Entity

                if (userDetails != null && userDetails.isEnabled()) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);

                    LogUtil.info(logger, "JWT_AUTH_SUCCESS", null, "username", username, "requestURI", request.getRequestURI());

                } else {
                    LogUtil.warn(logger, "JWT_AUTH_USER_NOT_FOUND", null, "username", username, "requestURI", request.getRequestURI());
                }
            }
        } else {
            LogUtil.warn(logger, "JWT_AUTH_INVALID_TOKEN", null, "requestURI", request.getRequestURI());
        }

        filterChain.doFilter(request, response);
        LogUtil.info(logger, "JWT_FILTER_END", null, "requestURI", request.getRequestURI());
    }
}
