package se.mojujo.blogservice.post.dto;

import jakarta.validation.constraints.NotBlank;

public record BlogPostCreationDTO(

        @NotBlank(message = "Title cannot be empty")
        String title,

        @NotBlank(message = "Content cannot be empty")
        String content,

        String imageUrl // Optional
) {
}
