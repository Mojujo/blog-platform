package se.mojujo.userservice.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public class UsernameAlreadyExistsException extends ErrorResponseFormat {
    public UsernameAlreadyExistsException(String message) {
        super(message, HttpStatus.CONFLICT, "Username already exists");
    }
}
