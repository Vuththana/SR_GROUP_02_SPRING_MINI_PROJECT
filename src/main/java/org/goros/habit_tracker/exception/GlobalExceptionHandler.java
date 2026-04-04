package org.goros.habit_tracker.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

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

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ProblemDetail handleInvalidEnum(HttpMessageNotReadableException ex, HttpServletRequest request) {
        Throwable cause = ex.getCause();
        if (cause instanceof com.fasterxml.jackson.databind.exc.InvalidFormatException invalidFormat
                && invalidFormat.getTargetType().isEnum()) {
            String allowedValues = Arrays.stream(invalidFormat.getTargetType().getEnumConstants())
                    .map(Object::toString)
                    .collect(Collectors.joining(", "));
            ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
            problemDetail.setType(URI.create("about:blank"));
            problemDetail.setTitle("Bad Request");
            problemDetail.setDetail("Cannot deserialize value of type `"
                    + invalidFormat.getTargetType().getSimpleName() + "` from String \""
                    + invalidFormat.getValue() + "\": not one of the allowed values [" + allowedValues + "]");
            problemDetail.setInstance(URI.create(request.getRequestURI()));
            problemDetail.setProperty("timestamp", Instant.now());
            return problemDetail;
        }
        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setType(URI.create("about:blank"));
        problemDetail.setTitle("Bad Request");
        problemDetail.setDetail(ex.getMessage());
        problemDetail.setInstance(URI.create(request.getRequestURI()));
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
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
    public ProblemDetail handleNotFoundException(NotFoundException e) {
        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, e.getMessage());
        problemDetail.setProperty("timestamp", Instant.now());
        return problemDetail;
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ProblemDetail handleConstraintViolationException(ConstraintViolationException ex, HttpServletRequest request) {
        Map<String, String> errors = new HashMap<>();

        ex.getConstraintViolations().forEach(violation -> {
            String propertyPath = violation.getPropertyPath().toString();
            String paramName = propertyPath.substring(propertyPath.lastIndexOf('.') + 1);
            errors.put(paramName, violation.getMessage());
        });

        ProblemDetail problemDetails = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetails.setInstance(URI.create(request.getRequestURI()));
        problemDetails.setProperty("timestamp", Instant.now());
        problemDetails.setProperty("errors", errors);

        return problemDetails;
    }
}
