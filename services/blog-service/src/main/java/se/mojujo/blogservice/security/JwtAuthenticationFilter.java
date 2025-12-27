package se.mojujo.blogservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import se.mojujo.blogservice.post.AuthenticatedUserDetails;
import se.mojujo.blogservice.util.LogUtil;

import java.io.IOException;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    private final JwtUtils jwtUtils;

    @Autowired
    public JwtAuthenticationFilter(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
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


        if (jwtUtils.validateJwtToken(token)) {
            UUID userId = jwtUtils.getUserIdFromJwtToken(token);
            String username = jwtUtils.getUsernameFromJwtToken(token);
            Set<String> roles = jwtUtils.getRolesFromJwtToken(token);

            if (userId != null && username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                Set<GrantedAuthority> authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toSet());


                LogUtil.info(logger, "JWT_ROLES_PARSED", null,
                        "username", username,
                        "roles", roles.toString(),
                        "requestURI", request.getRequestURI());

                AuthenticatedUserDetails authUser = new AuthenticatedUserDetails(userId, username, authorities);

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(authUser, null, authorities);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);

                LogUtil.info(logger, "JWT_AUTH_SUCCESS", null, "username", username, "requestURI", request.getRequestURI());
            }
        } else {
            LogUtil.warn(logger, "JWT_AUTH_INVALID_TOKEN", null, "requestURI", request.getRequestURI());
        }

        filterChain.doFilter(request, response);

        LogUtil.info(logger, "JWT_FILTER_END", null, "requestURI", request.getRequestURI());
    }
}
