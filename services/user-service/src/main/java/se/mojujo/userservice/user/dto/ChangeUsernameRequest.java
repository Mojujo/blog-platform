package se.mojujo.userservice.user.dto;

import jakarta.validation.constraints.NotBlank;

public record ChangeUsernameRequest(

        @NotBlank
        String newUsername
) {
}
