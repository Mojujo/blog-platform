package se.mojujo.userservice.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class UserNotFoundException extends ErrorResponseFormat {
    public UserNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "User not found");
    }
}
