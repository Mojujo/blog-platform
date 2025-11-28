package se.mojujo.userservice.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;
import se.mojujo.userservice.util.LogUtil;

import java.io.IOException;

@Component
public class CsrfCookieFilter extends OncePerRequestFilter {

    private static final Logger logger = LoggerFactory.getLogger(CsrfCookieFilter.class);


    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Only handle GET requests (or HEAD)
        if ("GET".equalsIgnoreCase(request.getMethod()) || "HEAD".equalsIgnoreCase(request.getMethod())) {
            CsrfToken csrfToken = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

            if (csrfToken != null) {
                Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");

                String token = csrfToken.getToken();
                if (cookie == null || token != null && !token.equals(cookie.getValue())) {
                    cookie = new Cookie("XSRF-TOKEN", token);
                    cookie.setPath("/");
                    cookie.setHttpOnly(false);
                    response.addCookie(cookie);

                    LogUtil.info(logger,
                            "CSRF_COOKIE_ADDED",
                            "Added CSRF cookie",
                            "token", token, "requestURI", request.getRequestURI());
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
