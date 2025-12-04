package se.mojujo.blogservice.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.web.ErrorResponse;

public abstract class ErrorResponseFormat extends RuntimeException implements ErrorResponse {

    private final HttpStatus status;
    private final String title;

    public ErrorResponseFormat(String message, HttpStatus status, String title) {
        super(message);
        this.status = status;
        this.title = title;
    }

    @Override
    public @NotNull HttpStatusCode getStatusCode() {
        return status;
    }

    @Override
    public @NotNull ProblemDetail getBody() {
        ProblemDetail detail = ProblemDetail.forStatusAndDetail(getStatusCode(), getMessage());
        detail.setTitle(title);
        return detail;
    }
}