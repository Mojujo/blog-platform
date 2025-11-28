package se.mojujo.userservice.exception;

import org.springframework.http.HttpStatus;

public class InvalidCredentialsException extends ErrorResponseFormat {
    public InvalidCredentialsException(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "Invalid username or password");
    }
}
