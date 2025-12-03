package se.mojujo.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.mojujo.userservice.user.CustomUser;
import se.mojujo.userservice.user.CustomUserDetails;
import se.mojujo.userservice.service.CustomUserService;
import se.mojujo.userservice.user.dto.CustomUserCreationDTO;
import se.mojujo.userservice.user.dto.CustomUserResponseDTO;
import se.mojujo.userservice.user.mapper.CustomUserMapper;

import java.util.Map;

@RestController
@RequestMapping
public class UserController {

    private final CustomUserService customUserService;
    private final CustomUserMapper customUserMapper;

    @Autowired
    public UserController(CustomUserService customUserService, CustomUserMapper customUserMapper) {
        this.customUserService = customUserService;
        this.customUserMapper = customUserMapper;
    }

    @PostMapping("/register")
    public ResponseEntity<CustomUserResponseDTO> registerUser(@Valid @RequestBody CustomUserCreationDTO dto) {
        CustomUserResponseDTO responseDTO = customUserService.createUser(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<CustomUserResponseDTO> getUserProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser customUser = userDetails.getCustomUser();

        CustomUserResponseDTO responseDTO = customUserMapper.toResponseDTO(customUser);

        return ResponseEntity.ok(responseDTO);
    }

    // Endpoint to make sure CSRF-tokens are securely generated on frontend
    @GetMapping("/csrf")
    public void getCsrfToken() {
        // No body needed; CsrfCookieFilter will set the XSRF-TOKEN cookie for frontend use
    }
}
