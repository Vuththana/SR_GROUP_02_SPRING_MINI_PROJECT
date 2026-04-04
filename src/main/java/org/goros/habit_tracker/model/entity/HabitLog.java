package org.goros.habit_tracker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.goros.habit_tracker.model.enums.HabitStatus;

import java.time.LocalDate;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitLog {
    private UUID habitLogId;
    private LocalDate logDate;
    private HabitStatus status;
    private Long xpEarned;
    private Habit habit;
}
