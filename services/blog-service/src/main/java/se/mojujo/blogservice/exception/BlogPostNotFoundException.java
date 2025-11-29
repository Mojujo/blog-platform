package se.mojujo.blogservice.exception;

import org.springframework.http.HttpStatus;

public class BlogPostNotFoundException extends ErrorResponseFormat {
    public BlogPostNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "Post not found");
    }
}
