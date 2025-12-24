package se.mojujo.blogservice.exception;

import org.springframework.http.HttpStatus;

public class BlogPostUpdateException extends ErrorResponseFormat {
    public BlogPostUpdateException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "Failed to update post");
    }
}
