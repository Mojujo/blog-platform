package se.mojujo.blogservice.post.mapper;

import org.springframework.stereotype.Component;
import se.mojujo.blogservice.post.BlogPost;
import se.mojujo.blogservice.post.dto.BlogPostCreationDTO;
import se.mojujo.blogservice.post.dto.BlogPostResponseDTO;

import java.util.UUID;

@Component
public class BlogPostMapper {

    public BlogPost toEntity(BlogPostCreationDTO dto, UUID userId, String authorUsername) {
        return new BlogPost(
                userId,
                authorUsername,
                dto.title(),
                dto.content(),
                dto.imageUrl()
        );
    }

    public BlogPostResponseDTO toResponse(BlogPost blogPost) {
        return new BlogPostResponseDTO(
                blogPost.getId(),
                blogPost.getUserId(),
                blogPost.getAuthorUsername(),
                blogPost.getTitle(),
                blogPost.getContent(),
                blogPost.getImageUrl(),
                blogPost.getCreatedDate(),
                blogPost.getUpdatedAt() // Can be null if not updated yet
        );
    }
}
