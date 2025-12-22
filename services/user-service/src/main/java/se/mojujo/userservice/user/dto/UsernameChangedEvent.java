package se.mojujo.userservice.user.dto;

import java.util.UUID;

public record UsernameChangedEvent(
        UUID userId,
        String newUsername
) {
}
