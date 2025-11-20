package se.mojujo.userservice.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.Set;
import java.util.UUID;

public record CustomUserResponseDTO(

        @NotNull
        UUID id,

        @Size(min = 2, max = 25, message = "Username length should be between 2-25")
        @NotBlank(message = "Username may not contain whitespace characters only")
        String username,

        @Email @NotBlank
        String email,

        @NotNull
        Set<String> roles
) {
}
