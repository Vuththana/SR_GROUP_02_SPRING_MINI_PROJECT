package org.goros.habit_tracker.service;

import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.entity.HabitLog;
import org.goros.habit_tracker.model.request.HabitLogRequest;

import java.util.UUID;

public interface HabitLogService {
    HabitLog getHabitLogByHabitId(UUID habitId, UUID appUserId);
    HabitLog saveHabitLog(HabitLogRequest habitLogRequest, UUID appUserId);
}
