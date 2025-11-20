package se.mojujo.userservice.user.dto;

import jakarta.validation.constraints.NotBlank;

// TODO VALIDATION & INJECTION PROTECTION
public record CustomUserLoginDTO (

        @NotBlank
        String username,

        @NotBlank
        String password
) {}
