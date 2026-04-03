package org.goros.habit_tracker.model.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ApiResponseVoid {
    private Boolean success;
    private HttpStatus status;
    private String message;
    private Instant timestamp;
}
