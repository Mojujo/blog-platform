package se.mojujo.blogservice.service;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import se.mojujo.blogservice.exception.BlogPostAccessDeniedException;
import se.mojujo.blogservice.exception.BlogPostNotFoundException;
import se.mojujo.blogservice.exception.InvalidBlogPostException;
import se.mojujo.blogservice.exception.UnauthorizedUserException;
import se.mojujo.blogservice.post.AuthenticatedUserDetails;
import se.mojujo.blogservice.post.BlogPost;
import se.mojujo.blogservice.post.dto.BlogPostCreationDTO;
import se.mojujo.blogservice.post.dto.BlogPostResponseDTO;
import se.mojujo.blogservice.post.mapper.BlogPostMapper;
import se.mojujo.blogservice.repository.BlogPostRepository;
import se.mojujo.blogservice.util.LogUtil;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
public class BlogPostService {

    private static final Logger logger = LoggerFactory.getLogger(BlogPostService.class);

    private final BlogPostRepository blogPostRepository;
    private final BlogPostMapper blogPostMapper;
    private final AuditService auditService;

    @Autowired
    public BlogPostService(BlogPostRepository blogPostRepository, BlogPostMapper blogPostMapper, AuditService auditService) {
        this.blogPostRepository = blogPostRepository;
        this.blogPostMapper = blogPostMapper;
        this.auditService = auditService;
    }

    @Transactional
    public BlogPostResponseDTO createBlogPost(BlogPostCreationDTO dto, Authentication authentication) {

        if (authentication == null) {
            LogUtil.warn(logger, "BLOG_CREATE_UNAUTHORIZED", "Unauthenticated user attempted to create a blog post");
            throw new UnauthorizedUserException("Authentication Required");
        }

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();

        if (dto.title() == null || dto.title().isBlank() || dto.content() == null || dto.content().isBlank()) {
            LogUtil.warn(logger, "BLOG_CREATE_INVALID", "Invalid blog post data provided", "userId", user.getUserId());
            throw new InvalidBlogPostException("Cannot create blog post without title or content");
        }

        LogUtil.info(logger, "BLOG_CREATE_START", null, "userId", user.getUserId());

        BlogPost post = blogPostMapper.toEntity(dto, user.getUserId(), user.getUsername());
        post.setCreatedDate(Instant.now());

        BlogPost savedPost = blogPostRepository.save(post);

        LogUtil.info(logger, "BLOG_CREATE_SUCCESS", null, "userId", user.getUserId(), "postId", savedPost.getId());

        Map<String, Object> auditData = new HashMap<>();
        auditData.put("userId", user.getUserId());
        auditData.put("postId", savedPost.getId());
        auditData.put("title", dto.title());
        auditData.put("content", dto.content());
        auditData.put("createdDate", savedPost.getCreatedDate());

        auditService.sendAuditEvent("POST_CREATED", auditData);

        return blogPostMapper.toResponse(savedPost);
    }

    public BlogPostResponseDTO updatePost(BlogPostCreationDTO dto, Authentication authentication, UUID postId) {

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();

        LogUtil.info(logger, "BLOG_UPDATE_START", null, "userId", user.getUserId(), "postId", postId);

        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> {
                    LogUtil.warn(logger,
                            "BLOG_UPDATE_NOT_FOUND",
                            "Blog post not found",
                            "postId", postId, "userId", user.getUserId());

                    return new BlogPostNotFoundException(postId);
                });

        if (!post.getUserId().equals(user.getUserId())) {
            LogUtil.warn(logger,
                    "BLOG_UPDATE_ACCESS_DENIED",
                    "User attempted to update another user's post",
                    "postId", postId, "userId", user.getUserId(), "ownerId", post.getUserId());

            throw new BlogPostAccessDeniedException("User is not allowed to delete this post");
        }

        post.setTitle(dto.title());
        post.setContent(dto.content());
        post.setImageUrl(dto.imageUrl());
        post.setUpdatedAt(Instant.now());

        BlogPost savedPost = blogPostRepository.save(post);

        LogUtil.info(logger, "BLOG_UPDATE_SUCCESS", null, "postId", postId, "userId", user.getUserId());

        return blogPostMapper.toResponse(savedPost);
    }

    public void updatePostAuthor(UUID userId, String newUsername) {
        List<BlogPost> posts = blogPostRepository.findAllByUserId(userId);

        posts.forEach(post -> post.setAuthorUsername(newUsername));
        blogPostRepository.saveAll(posts);
    }

    public void deletePost(Authentication authentication, UUID postId) {

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();

        LogUtil.info(logger, "BLOG_DELETE_START", null, "userId", user.getUserId(), "postId", postId);

        BlogPost post = blogPostRepository.findById(postId)
                .orElseThrow(() -> {
                    LogUtil.warn(logger,
                            "BLOG_DELETE_NOT_FOUND",
                            "Blog post not found",
                            "postId", postId, "userId", user.getUserId());

                    return new BlogPostNotFoundException(postId);
                });

        if (!post.getUserId().equals(user.getUserId())) {
            LogUtil.warn(logger,
                    "BLOG_DELETE_ACCESS_DENIED",
                    "User attempted to delete another user's post",
                    "postId", postId, "userId", user.getUserId(), "ownerId", post.getUserId());

            throw new SecurityException("Cannot delete another user's post");
        }

        blogPostRepository.delete(post);

        LogUtil.info(logger, "BLOG_DELETE_SUCCESS", null, "postId", postId, "userId", user.getUserId());
    }

    public Page<BlogPostResponseDTO> getAllPostsOrdered(Authentication authentication, int page, int size) {

        AuthenticatedUserDetails user = (AuthenticatedUserDetails) authentication.getPrincipal();

        LogUtil.info(logger, "BLOG_GET_START", null, "userId", user.getUserId());

        Pageable pageable = PageRequest.of(page, size);
        Page<BlogPost> posts = blogPostRepository.findAllByUserIdOrderByCreatedDateDesc(user.getUserId(), pageable);

        return posts.map(blogPostMapper::toResponse);
    }

    public Page<BlogPostResponseDTO> getAllPosts(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdDate").descending());
        Page<BlogPost> posts = blogPostRepository.findAll(pageable);
        return posts.map(blogPostMapper::toResponse);
    }
}
