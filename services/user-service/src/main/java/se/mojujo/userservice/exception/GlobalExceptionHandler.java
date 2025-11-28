package se.mojujo.userservice.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            UsernameAlreadyExistsException.class,
            EmailAlreadyExistsException.class,
            UserNotFoundException.class
    })
    public ResponseEntity<ProblemDetail> handleErrorResponse(@NotNull ErrorResponse e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .headers(e.getHeaders()) // ErrorResponse supports custom headers
                .body(e.getBody());
    }

    // Fallback handler for unexpected exceptions
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetail> handleGeneralException(@NotNull Exception e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage());
        detail.setTitle("Internal Server Error");
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(detail);
    }
}
