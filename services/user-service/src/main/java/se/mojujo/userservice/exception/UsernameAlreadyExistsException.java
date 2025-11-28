package se.mojujo.userservice.exception;

import org.springframework.http.HttpStatus;

public class UsernameAlreadyExistsException extends ErrorResponseFormat {
    public UsernameAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, "Username already exists");
    }
}
