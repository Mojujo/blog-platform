package se.mojujo.blogservice.exception;

import org.springframework.http.HttpStatus;

public class BlogPostAccessDeniedException extends ErrorResponseFormat {
    public BlogPostAccessDeniedException(String message) {
        super(message, HttpStatus.FORBIDDEN, "Cannot edit others' posts");
    }
}
