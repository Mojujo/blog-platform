package se.mojujo.blogservice.post;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import se.mojujo.blogservice.post.dto.BlogPostCreationDTO;
import se.mojujo.blogservice.post.dto.BlogPostResponseDTO;
import se.mojujo.blogservice.post.mapper.BlogPostMapper;
import se.mojujo.blogservice.repository.BlogPostRepository;

import java.time.Instant;
import java.util.UUID;

@Service
public class BlogPostService {

    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, BlogPostMapper blogPostMapper) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostMapper = blogPostMapper;
    }

    @Transactional
    public BlogPostResponseDTO createBlogPost(BlogPostCreationDTO dto, Authentication authentication) {

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();

        BlogPost post = blogPostMapper.toEntity(dto, user.getUserId());
        post.setCreatedDate(Instant.now());

        BlogPost savedPost = blogPostRepository.save(post);

        return blogPostMapper.toResponse(savedPost);
    }

    public BlogPostResponseDTO updatePost(BlogPostCreationDTO dto, Authentication authentication, UUID postId) {

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not found"));

        if (!post.getUserId().equals(user.getUserId())) {
            throw new SecurityException("Cannot edit another user's post");
        }

        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setImageUrl(dto.imageUrl());
        post.setUpdatedAt(Instant.now());

        BlogPost savedPost = blogPostRepository.save(post);

        return blogPostMapper.toResponse(savedPost);
    }

    public void deletePost(Authentication authentication, UUID postId) {

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();
        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> new EntityNotFoundException("Post with id " + postId + " not found"));

        if (!post.getUserId().equals(user.getUserId())) {
            throw new SecurityException("Cannot delete another user's post");
        }

        blogPostRepository.delete(post);
    }
}
