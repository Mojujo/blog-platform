package se.mojujo.userservice.exception;

import org.springframework.http.HttpStatus;

public class FileStorageException extends ErrorResponseFormat {
    public FileStorageException(String message) {
        super(message, HttpStatus.BAD_REQUEST, "Failed to upload image");
    }
}
