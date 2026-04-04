package org.goros.habit_tracker.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.goros.habit_tracker.model.enums.Frequency;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitRequest {
    private String title;
    private String description;
    private Frequency frequency;
    @JsonIgnore
    private UUID appUserId;
}
