package org.goros.habit_tracker.model.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.enums.HabitStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitLogRequest {
    private HabitStatus status;
    private UUID habitId;
}
