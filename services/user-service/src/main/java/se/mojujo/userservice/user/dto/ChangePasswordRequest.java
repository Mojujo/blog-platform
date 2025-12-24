package se.mojujo.userservice.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank
        String oldPassword,

        @NotBlank
        @Size(max = 40, message = "Maximum length of password exceeded")
        String newPassword
) {
}
