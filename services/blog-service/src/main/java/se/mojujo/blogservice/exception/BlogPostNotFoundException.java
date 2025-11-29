package se.mojujo.blogservice.exception;

import org.jetbrains.annotations.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

import java.util.UUID;

public class BlogPostNotFoundException extends ErrorResponseFormat {

    private final UUID postId;

    public BlogPostNotFoundException(UUID id) {
        super(
                "Blog post with ID %s not found".formatted(id),
                HttpStatus.NOT_FOUND,
                "Post Not Found"
        );
        this.postId = id;
    }

    public BlogPostNotFoundException(String message) {
        super(message, HttpStatus.NOT_FOUND, "Post Not Found");
        this.postId = null;
    }

    @Override
    public @NotNull ProblemDetail getBody() {
        ProblemDetail detail = super.getBody();
        if (postId != null) {
            detail.setProperty("postId", postId.toString());
        }
        return detail;
    }
}
