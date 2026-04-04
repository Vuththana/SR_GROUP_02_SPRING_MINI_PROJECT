package org.goros.habit_tracker.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.request.HabitRequest;

import java.util.List;
import java.util.UUID;

public interface HabitService {
    List<Habit> getAllHabits(@Positive(message = "Page must be positive") Integer page, @Positive(message = "Size must be positive") Integer size);
    
    Habit getHabitById(UUID habitId);

    Habit saveHabit(@Valid HabitRequest habitRequest);
}
