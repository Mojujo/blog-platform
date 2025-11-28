package se.mojujo.userservice.exception;

import org.springframework.http.HttpStatus;

public class EmailAlreadyExistsException extends ErrorResponseFormat {
    public EmailAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, "Email already exists");
    }
}
