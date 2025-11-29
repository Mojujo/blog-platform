package se.mojujo.blogservice.exception;

import org.springframework.http.HttpStatus;

public class UnauthorizedUserException extends ErrorResponseFormat {
    public UnauthorizedUserException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "Unauthorized");
    }
}
