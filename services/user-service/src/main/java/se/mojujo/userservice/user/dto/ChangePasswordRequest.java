package se.mojujo.userservice.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record ChangePasswordRequest(

        @NotBlank
        String oldPassword,

        @NotBlank
        @Pattern(
                regexp = "^" +
                        "(?=.*[a-z])" +        // at least one lowercase letter
                        "(?=.*[A-Z])" +        // at least one uppercase letter
                        "(?=.*[0-9])" +        // at least one digit
                        "(?=.*[ @$!%*?&])" +   // at least one special character
                        ".+$",                 // one or more characters, until end
                message = "Password must contain at least one uppercase, one lowercase, one digit, and one special character"
        )
        @Size(max = 40, message = "Maximum length of password exceeded")
        String newPassword
) {
}
