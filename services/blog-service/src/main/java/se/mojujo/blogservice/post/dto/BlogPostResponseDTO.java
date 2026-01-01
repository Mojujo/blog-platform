package se.mojujo.blogservice.post.dto;

import java.time.Instant;
import java.util.UUID;

public record BlogPostResponseDTO(

        UUID id,
        UUID userId,
        String authorUsername,
        String title,
        String content,
        String imageUrl,
        Instant createdDate,
        Instant updatedAt, // null if never updated
        Boolean isOwner

        ) {}
