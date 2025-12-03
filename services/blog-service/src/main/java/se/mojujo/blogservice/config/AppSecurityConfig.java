package se.mojujo.blogservice.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfigurationSource;
import se.mojujo.blogservice.security.CsrfCookieFilter;
import se.mojujo.blogservice.security.JwtAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class AppSecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final CsrfCookieFilter csrfCookieFilter;

    @Autowired
    public AppSecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, CsrfCookieFilter csrfCookieFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
        this.csrfCookieFilter = csrfCookieFilter;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public CsrfTokenRequestAttributeHandler csrfTokenRequestAttributeHandler() {
        CsrfTokenRequestAttributeHandler handler = new CsrfTokenRequestAttributeHandler();
        handler.setCsrfRequestAttributeName("_csrf");
        return handler;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                // CSRF Enabled, token stored in cookie
                .csrf(csrf -> csrf
                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                        .csrfTokenRequestHandler(csrfTokenRequestAttributeHandler())
                )

                // Route Authorization
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/post").hasAnyRole("USER", "ADMIN")
                        .anyRequest().authenticated()
                )
                // TODO AUTH LOGOUT?
                // Stateless session
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                )

                // Add JWT Filter and CsrfCookieFilter before UsernameAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(csrfCookieFilter, CsrfFilter.class);

        return httpSecurity.build();
    }
}
