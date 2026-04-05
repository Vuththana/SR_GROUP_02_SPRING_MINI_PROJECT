package org.goros.habit_tracker.service;

import jakarta.validation.constraints.Positive;
import org.goros.habit_tracker.model.entity.Achievement;

import java.util.List;
import java.util.UUID;

public interface AchievementService {
    List<Achievement> getAllAchievements(@Positive Long page, @Positive Long size);
    List<Achievement> getAllAchievementByAppUserId(@Positive Long page, @Positive Long size);
    void checkAchievements(UUID appUserId, int xp);
}
