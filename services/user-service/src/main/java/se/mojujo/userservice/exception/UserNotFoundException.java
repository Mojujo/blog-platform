package se.mojujo.userservice.exception;

import org.springframework.http.HttpStatus;

public class UserNotFoundException extends ErrorResponseFormat {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "User not found");
    }
}
