package se.mojujo.blogservice.exception;

import jakarta.validation.ConstraintViolationException;
import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({
            BlogPostAccessDeniedException.class,
            BlogPostNotFoundException.class,
            BlogPostUpdateException.class,
            InvalidBlogPostException.class,
            UnauthorizedUserException.class,
            FileStorageException.class,
    })
    public ResponseEntity<ProblemDetail> handleErrorResponse(@NotNull ErrorResponse e) {
        return ResponseEntity
                .status(e.getStatusCode())
                .headers(e.getHeaders()) // ErrorResponse supports custom headers
                .body(e.getBody());
    }

    // Handling Validation Errors
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetail> handleValidationException(MethodArgumentNotValidException e) {

        String errors = e.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining("; "));

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors);
        detail.setTitle("Validation Failed");

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(detail);
    }

    // Invalid JSON / Body exceptions
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ProblemDetail> handleJsonParseError(HttpMessageNotReadableException e) {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(
                HttpStatus.BAD_REQUEST,
                "Malformed or unreadable JSON request body"
        );
        detail.setTitle("Invalid Request Body");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detail);
    }

    // Path variable validation
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ProblemDetail> handleConstraintViolation(ConstraintViolationException e) {

        String errors = e.getConstraintViolations()
                .stream()
                .map(v -> v.getPropertyPath() + ": " + v.getMessage())
                .collect(Collectors.joining("; "));

        ProblemDetail detail = ProblemDetail.forStatusAndDetail(HttpStatus.BAD_REQUEST, errors);
        detail.setTitle("Constraint Violation");

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(detail);
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
