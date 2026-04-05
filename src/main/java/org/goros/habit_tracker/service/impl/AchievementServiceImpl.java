package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.exception.NotFoundException;
import org.goros.habit_tracker.model.entity.Achievement;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.repository.AchievementRepository;
import org.goros.habit_tracker.repository.UserAchievementRepository;
import org.goros.habit_tracker.service.AchievementService;
import org.goros.habit_tracker.service.AppUserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AchievementServiceImpl implements AchievementService {
    private final AchievementRepository achievementRepository;
    private final UserAchievementRepository userAchievementRepository;

    @Override
    public List<Achievement> getAllAchievements(Long page, Long size) {

        Long offset = (page - 1) * size;

        List<Achievement> achievements = achievementRepository.getAllAchievements(offset, size);
        if (achievements.isEmpty()) throw new NotFoundException("no achievement found");

        return achievements;
    }

    @Override
    public List<Achievement> getAllAchievementByAppUserId(Long page, Long size) {

        AppUser appUser = (AppUser) Objects.requireNonNull(
                SecurityContextHolder.getContext().getAuthentication()
        ).getPrincipal();

        UUID userId = appUser.getAppUserId();

        int currentXp = Math.toIntExact(appUser.getXp());

        checkAchievements(userId, currentXp);

        Long offset = (page - 1) * size;
        List<Achievement> achievements = achievementRepository.getAllAchievementByAppUserId(offset, size, userId);

        if (achievements.isEmpty()) {
            throw new NotFoundException("no achievement found");
        }

        return achievements;
    }

    @Override
    public void checkAchievements(UUID appUserId, int xp) {
        List<Achievement> achievements = achievementRepository.findByRequiredXpLessThanEqual(xp);

        List<UUID> unlocked =
                userAchievementRepository.findAchievementIdsByUserId(appUserId);

        for (Achievement achievement : achievements) {
            if (!unlocked.contains(achievement.getAchievementId())) {
                userAchievementRepository.insert(appUserId, achievement.getAchievementId());
            }
        }
    }
}
