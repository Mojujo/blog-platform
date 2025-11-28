package se.mojujo.userservice.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import se.mojujo.userservice.security.AuthService;
import se.mojujo.userservice.security.JwtUtils;
import se.mojujo.userservice.security.dto.AuthResponseDTO;
import se.mojujo.userservice.user.CustomUserDetails;
import se.mojujo.userservice.user.dto.CustomUserLoginDTO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody CustomUserLoginDTO loginDTO, HttpServletResponse response) {

        // Login Attempt
        AuthResponseDTO authResponse = authService.login(loginDTO.username(), loginDTO.password());

        // Set Cookie
        Cookie authCookie = new Cookie("authToken", authResponse.token());
        authCookie.setHttpOnly(true);
        authCookie.setSecure(false);
        authCookie.setMaxAge(3600);
        authCookie.setPath("/");
        authCookie.setAttribute("SameSite", "Lax");
        response.addCookie(authCookie);

        return ResponseEntity.ok(Map.of(
                "username", loginDTO.username(),
                "roles", authResponse.roles(),
                "token", authResponse.token()
        ));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        return ResponseEntity.ok(Map.of("message", "You've been logged out"));
    }
}
