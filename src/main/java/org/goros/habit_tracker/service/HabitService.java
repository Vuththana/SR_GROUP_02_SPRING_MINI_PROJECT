package org.goros.habit_tracker.service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.request.HabitRequest;
import org.goros.habit_tracker.model.response.AppUserResponse;

import java.util.List;
import java.util.UUID;

public interface HabitService {
//    List<Habit> getAllHabits(@Positive(message = "Page must be positive") Integer page, @Positive(message = "Size must be positive") Integer size);
    
    Habit getHabitById(UUID habitId ,UUID currentAppUserId);

    Habit saveHabit(@Valid HabitRequest habitRequest, AppUserResponse currentUser);

    void deleteHabitById(UUID habitId , UUID currentAppUserId);


    Habit updateHabitById(UUID habitId, HabitRequest habitRequest , UUID currentAppUserId);

    List<Habit> getAllHabits(@Positive(message = "Page must be positive") Integer page, @Positive(message = "Size must be positive") Integer size, UUID currentAppUserId);
}
