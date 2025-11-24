package se.mojujo.blogservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import se.mojujo.blogservice.post.BlogPostService;
import se.mojujo.blogservice.post.dto.BlogPostCreationDTO;
import se.mojujo.blogservice.post.dto.BlogPostResponseDTO;


@RestController
@RequestMapping
public class BlogPostController {

    private final BlogPostService blogPostService;

    @Autowired
    public BlogPostController(BlogPostService blogPostService) {
        this.blogPostService = blogPostService;
    }

    @PostMapping("/post")
    public BlogPostResponseDTO createPost(@RequestBody BlogPostCreationDTO dto, Authentication authentication) {
        return blogPostService.createBlogPost(dto, authentication);
    }
}
