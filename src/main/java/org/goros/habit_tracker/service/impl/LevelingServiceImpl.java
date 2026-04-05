package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.goros.habit_tracker.repository.AppUserRepository;
import org.goros.habit_tracker.service.AchievementService;
import org.goros.habit_tracker.service.LevelingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class LevelingServiceImpl implements LevelingService {

    private final AppUserRepository appUserRepository;
    private final AchievementService achievementService;

    @Override
    @Transactional
    public AppUserResponse addXp(UUID appUserId, int xp) {
        AppUserResponse userResponse = appUserRepository.getUserById(appUserId);
        int newXp = Math.toIntExact(userResponse.getXp() + xp);
        userResponse.setXp((long) newXp);

        int newLevel = calculateLevel(newXp);

        appUserRepository.updateXpAndLevel(appUserId, newXp, newLevel);

        achievementService.checkAchievements(appUserId, newXp);
        return userResponse;
    }

    private int calculateLevel(int xp) {
        return xp / 100;
    }
}
