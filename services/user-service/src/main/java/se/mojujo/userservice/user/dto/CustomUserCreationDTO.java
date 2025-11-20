package se.mojujo.userservice.user.dto;

import jakarta.validation.constraints.*;
import se.mojujo.userservice.user.authority.UserRole;

import java.util.Set;

public record CustomUserCreationDTO(

        @Size(min = 2, max = 25, message = "Username length should be between 2-25")
        @NotBlank
        String username,

        @NotBlank
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$", message = "Invalid email address")
        String email,

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
        String password,

        // TODO @AssertTrue acceptAppTerms

        @Size(min = 1)
        @NotNull
        Set<UserRole> roles
) {}
