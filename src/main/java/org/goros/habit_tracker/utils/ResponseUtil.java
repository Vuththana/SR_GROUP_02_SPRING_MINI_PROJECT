package org.goros.habit_tracker.utils;

import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.model.response.ApiResponseVoid;
import org.springframework.http.HttpStatus;

import java.time.Instant;

public class ResponseUtil {
    public static<T>ApiResponse<T> success(HttpStatus status, String message, T payload) {
        return ApiResponse.<T>builder().success(true).status(status).message(message).payload(payload).timestamp(Instant.now()).build();
    }

    public static<T> ApiResponse<T> notFound(String message) {
        return ApiResponse.<T>builder().success(false).status(HttpStatus.NOT_FOUND).message(message).timestamp(Instant.now()).build();
    }

    public static ApiResponseVoid successVoid(HttpStatus status, String message) {
        return ApiResponseVoid.builder().success(true).status(status).message(message).timestamp(Instant.now()).build();
    }

    public static ApiResponseVoid errorVoid(HttpStatus status, String message) {
        return ApiResponseVoid.builder().success(false).status(status).message(message).timestamp(Instant.now()).build();
    }
}
