package org.goros.habit_tracker.service;

import org.goros.habit_tracker.model.response.AppUserResponse;

import java.util.UUID;

public interface LevelingService {
   AppUserResponse addXp(UUID userId, int xp);
}
