package se.mojujo.blogservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidBlogPostException extends ErrorResponseFormat {
    public InvalidBlogPostException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "Validation error");
    }
}
