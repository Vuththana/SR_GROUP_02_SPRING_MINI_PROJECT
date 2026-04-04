package org.goros.habit_tracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.net.URI;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler({UserNotVerifiedException.class, BadRequestException.class, BadCredentialsException.class, UsernameNotFoundException.class})
    public ProblemDetail handleBadRequestException(Exception ex, HttpServletRequest request) {
        ProblemDetail problemDetails = ProblemDetail.forStatus(400);
        problemDetails.setType(URI.create("about:blank"));
        problemDetails.setTitle("Bad Request");
        problemDetails.setDetail(ex.getMessage());
        problemDetails.setInstance(URI.create(request.getRequestURI()));
        problemDetails.setProperty("timestamp", Instant.now());
        return problemDetails;
    }

    @ExceptionHandler(ConflictException.class)
    public ProblemDetail handleBadRequestException(ConflictException ex, HttpServletRequest request) {
        ProblemDetail problemDetails = ProblemDetail.forStatus(409);
        problemDetails.setType(URI.create("about:blank"));
        problemDetails.setTitle("Duplicated User Entry");
        problemDetails.setDetail(ex.getMessage());
        problemDetails.setInstance(URI.create(request.getRequestURI()));
        problemDetails.setProperty("timestamp", Instant.now());
        return problemDetails;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidationException(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problemDetails = ProblemDetail.forStatus(400);
        problemDetails.setTitle("Validation Failed");
        problemDetails.setInstance(URI.create(request.getRequestURI()));
        Map<String, String> errors = new HashMap<>();
        for (FieldError fieldError : ex.getBindingResult().getFieldErrors()) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        problemDetails.setProperty("errors", errors);
        return problemDetails;
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ProblemDetail handleMethodValidationException(HandlerMethodValidationException e) {
        Map<String, String> errors = new HashMap<>();

        e.getParameterValidationResults().forEach(parameterError -> {
            String paramName = parameterError.getMethodParameter().getParameterName();

            for (var messageError : parameterError.getResolvableErrors()) {
                errors.put(paramName, messageError.getDefaultMessage());
            }
        });

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Method Parameter Validation Failed");
        problemDetail.setProperties(Map.of(
                "timestamp", LocalDateTime.now(),
                "errors", errors
        ));

        return problemDetail;
    }

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFoundException(NotFoundException ex, HttpServletRequest request) {
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setTitle("Not Found");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
