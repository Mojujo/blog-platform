package se.mojujo.blogservice.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.mojujo.blogservice.post.BlogPostService;
import se.mojujo.blogservice.post.dto.BlogPostCreationDTO;
import se.mojujo.blogservice.post.dto.BlogPostResponseDTO;
import se.mojujo.blogservice.util.LogUtil;

import java.util.UUID;


@RestController
@RequestMapping
@PreAuthorize("hasAnyRole('USER','ADMIN')")
public class BlogPostController {

    private static final Logger logger = LoggerFactory.getLogger(BlogPostController.class);

    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping("/post")
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
}
