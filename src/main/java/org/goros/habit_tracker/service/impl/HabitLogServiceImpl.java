package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.exception.BadRequestException;
import org.goros.habit_tracker.exception.NotFoundException;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.entity.HabitLog;
import org.goros.habit_tracker.model.enums.HabitStatus;
import org.goros.habit_tracker.model.request.HabitLogRequest;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.goros.habit_tracker.repository.HabitLogRepository;
import org.goros.habit_tracker.repository.HabitRepository;
import org.goros.habit_tracker.service.HabitLogService;
import org.goros.habit_tracker.service.LevelingService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HabitLogServiceImpl implements HabitLogService {
    private final HabitLogRepository habitLogRepository;
    private final HabitRepository habitRepository;
    private final LevelingService levelingService;

    @Override
    public HabitLog getHabitLogByHabitId(UUID habitId, UUID appUserId) {
        HabitLog habitLog = habitLogRepository.getHabitLogByHabitId(habitId, appUserId);
        if (habitLog == null) {
            throw new NotFoundException("No habit log found with that ID");
        }
        return habitLog;
    }

    @Override
    public HabitLog saveHabitLog(HabitLogRequest habitLogRequest, UUID appUserId) {

        Habit habit = habitRepository.getHabitById(habitLogRequest.getHabitId(), appUserId);
        if (habit == null) throw new NotFoundException("Habit not found");

        LocalDate today = LocalDate.now();
        HabitLog existing = habitLogRepository.findByHabitIdAndDate(habit.getHabitId(), today, appUserId);

        long xpToAdd = habitLogRequest.getStatus() == HabitStatus.COMPLETED ? 10L : 0L;
        HabitLog habitLogToReturn;

        if (existing == null) {
            HabitLog habitLog = new HabitLog();
            habitLog.setHabitLogId(UUID.randomUUID());
            habitLog.setLogDate(today);
            habitLog.setStatus(habitLogRequest.getStatus());
            habitLog.setXpEarned(xpToAdd);
            habitLog.setHabit(habit);

            habitLogToReturn = habitLogRepository.saveHabitLog(habitLog, appUserId);
        } else {
            if (existing.getStatus() == HabitStatus.COMPLETED)
                throw new BadRequestException("Habit already completed today");

            existing.setStatus(habitLogRequest.getStatus());
            existing.setXpEarned(xpToAdd);
            existing.setHabit(habit);

            habitLogRepository.updateHabitLog(existing);
            habitLogToReturn = existing;
        }

        if (xpToAdd > 0) {
            AppUserResponse updatedUser = levelingService.addXp(appUserId, (int) xpToAdd);
        }

        return habitLogToReturn;
    }}
