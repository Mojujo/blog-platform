package se.mojujo.userservice.exception;

import org.springframework.http.HttpStatus;

public class AuthorizationExpired extends ErrorResponseFormat {
    public AuthorizationExpired(String message) {
        super(message, HttpStatus.UNAUTHORIZED, "Authorization expired");
    }
}
