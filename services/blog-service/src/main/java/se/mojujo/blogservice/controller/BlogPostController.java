package se.mojujo.blogservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.mojujo.blogservice.service.BlogPostService;
import se.mojujo.blogservice.post.dto.BlogPostCreationDTO;
import se.mojujo.blogservice.post.dto.BlogPostResponseDTO;
import se.mojujo.blogservice.util.LogUtil;

import java.util.UUID;


@RestController
@RequestMapping("/post")
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class BlogPostController {

    private static final Logger logger = LoggerFactory.getLogger(BlogPostController.class);

    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping
    public BlogPostResponseDTO createPost(@RequestBody BlogPostCreationDTO dto, Authentication authentication) {

        BlogPostResponseDTO response = blogPostService.createBlogPost(dto, authentication);

        LogUtil.info(logger, "POST_CREATE_RESPONSE", null, "postId", response.id(), "title", response.title());

        return response;
    }

    @PutMapping("/{postId}")
    public BlogPostResponseDTO updatePost(
            @PathVariable UUID postId,
            @RequestBody BlogPostCreationDTO dto,
            Authentication authentication) {

        BlogPostResponseDTO response = blogPostService.updatePost(dto, authentication, postId);

        LogUtil.info(logger, "POST_UPDATE_RESPONSE", null, "postId", response.id(), "title", response.title());

        return response;
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable UUID postId, Authentication authentication) {

        blogPostService.deletePost(authentication, postId);

        LogUtil.info(logger, "POST_DELETE_SUCCESS", null, "postId", postId);
    }

    @GetMapping("/{postId}")
    public ResponseEntity<BlogPostResponseDTO> getPost(@PathVariable UUID postId, Authentication authentication) {
        return ResponseEntity.ok(blogPostService.getPostById(authentication, postId));
    }

    @GetMapping
    public Page<BlogPostResponseDTO> getPosts(
            Authentication authentication,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return blogPostService.getAllPostsOrdered(authentication, page, size);
    }

    // Fetch all posts
    @GetMapping("/feed")
    public ResponseEntity<Page<BlogPostResponseDTO>> getFeed(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Page<BlogPostResponseDTO> posts = blogPostService.getAllPosts(page, size);

        LogUtil.info(logger, "FEED_FETCHED", null, "posts", posts.getTotalElements(), posts.getTotalPages());

        return ResponseEntity.ok(posts);
    }
}
