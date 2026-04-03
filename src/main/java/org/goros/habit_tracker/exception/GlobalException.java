package org.goros.habit_tracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.Instant;

@RestControllerAdvice
public class GlobalException {
    @ExceptionHandler(UserNotVerifiedException.class)
    public ProblemDetail handleUserNotVerifiedException(UserNotVerifiedException ex, HttpServletRequest request) {
        ProblemDetail problemDetails = ProblemDetail.forStatus(400);
        problemDetails.setType(URI.create("about:blank"));
        problemDetails.setTitle("Bad Request");
        problemDetails.setDetail(ex.getMessage());
        problemDetails.setInstance(URI.create(request.getRequestURI()));
        problemDetails.setProperty("timestamp", Instant.now());
        return problemDetails;
    }
}
