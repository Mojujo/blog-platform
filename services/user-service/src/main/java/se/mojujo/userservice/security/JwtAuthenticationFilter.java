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

        logger.debug(" JwtAuthenticationFilter Start ");

        // Extract token
        String token = jwtUtils.extractJwtFromCookie(request);
        if (token == null) {
            token = jwtUtils.extractJwtFromRequest(request); // Fallback to auth header
        }

        if (token == null) {
            logger.debug("No JWT token found in request");
            filterChain.doFilter(request, response);
            return;
        }

        logger.debug("JWT token found: {}", token);

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

                    logger.debug("Authenticated (Database verified) user '{}'", username);
                } else {
                    logger.warn("User '{}' not found or disabled", username);
                }
            }
        } else {
            logger.warn("Invalid JWT token");
        }

        filterChain.doFilter(request, response);
        logger.debug(" JwtAuthenticationFilter End ");
    }
}
