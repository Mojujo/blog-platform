package se.mojujo.userservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import se.mojujo.userservice.user.CustomUser;
import se.mojujo.userservice.user.CustomUserDetails;
import se.mojujo.userservice.service.CustomUserService;
import se.mojujo.userservice.user.dto.*;
import se.mojujo.userservice.user.mapper.CustomUserMapper;

import java.util.UUID;

@RestController
@RequestMapping("/user")
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
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<CustomUserResponseDTO> getUserProfile(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        CustomUser customUser = userDetails.getCustomUser();

        CustomUserResponseDTO responseDTO = customUserMapper.toResponseDTO(customUser);

        return ResponseEntity.ok(responseDTO);
    }

    // Endpoint to make sure CSRF-tokens are securely generated on the frontend
    @GetMapping("/csrf")
    public void getCsrfToken() {
        // No body needed; CsrfCookieFilter will set the XSRF-TOKEN cookie for frontend use
    }

    @PatchMapping("/username")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> changeUsername(@Valid @RequestBody ChangeUsernameRequest request, Authentication authentication) {
        UUID userId = ((CustomUserDetails) authentication.getPrincipal()).getCustomUser().getId();

        customUserService.changeUsername(userId, request.newUsername());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/email")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> changeEmail(@Valid @RequestBody ChangeEmailRequest request, Authentication authentication) {
        UUID userId = ((CustomUserDetails) authentication.getPrincipal()).getCustomUser().getId();

        customUserService.changeEmail(userId, request.newEmail());

        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/password")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> changePassword(@Valid @RequestBody ChangePasswordRequest request, Authentication authentication) {
        UUID userId = ((CustomUserDetails) authentication.getPrincipal()).getCustomUser().getId();

        customUserService.changePassword(userId, request.oldPassword(), request.newPassword());

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/profile-picture")
    @PreAuthorize("hasAnyRole('USER','ADMIN')")
    public ResponseEntity<Void> changeProfilePicture(@RequestParam("file") MultipartFile imageFile, Authentication authentication) {
        UUID userId = ((CustomUserDetails) authentication.getPrincipal()).getCustomUser().getId();

        customUserService.changeProfilePicture(userId, imageFile);

        return ResponseEntity.noContent().build();
    }
}
