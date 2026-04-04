package org.goros.habit_tracker.service.impl;

import java.util.List;
import java.util.UUID;

import org.goros.habit_tracker.exception.BadRequestException;
import org.goros.habit_tracker.exception.NotFoundException;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.enums.Frequency;
import org.goros.habit_tracker.model.request.HabitRequest;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.goros.habit_tracker.repository.HabitRepository;
import org.goros.habit_tracker.service.HabitService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {
    private final HabitRepository habitRepository;

    @Override
    public List<Habit> getAllHabits(Integer page, Integer size, UUID currentAppUserId) {
        int offset = (page - 1) * size;
        List<Habit> habits = habitRepository.getAllHabits(offset, size, currentAppUserId);
        if (habits.isEmpty()) throw new NotFoundException("no habits found");
        return habits;
    }

    @Override
    public Habit getHabitById(UUID habitId , UUID currentAppUserId) {
        Habit habit = habitRepository.getHabitById(habitId , currentAppUserId);
        if(habit == null) {
            throw new NotFoundException("Habit With ID [" + habitId + "] Not Found!");
        }
        return habit;
    }

    @Override
    public Habit saveHabit(HabitRequest habitRequest, AppUserResponse currentUser) {
        habitRequest.setAppUserId(currentUser.getAppUserId());
        return habitRepository.saveHabit(habitRequest);
    }

    @Override
    public void deleteHabitById(UUID habitId , UUID currentAppUserId) {
        int habit = habitRepository.deleteHabitById(habitId , currentAppUserId);
        if(habit == 0) {
            throw new NotFoundException("Habit With ID [" + habitId + "] Not Found!");
        }
    }

    @Override
    public Habit updateHabitById(UUID habitId, HabitRequest habitRequest ,UUID currentAppUserId) {
        Habit habit = habitRepository.updateHabitById(habitId , habitRequest , currentAppUserId);
        if(habit == null) {
            throw new NotFoundException("Habit With ID [" + habitId + "] Not Found!");
        }
        boolean isValid = false;
        for (Frequency f : Frequency.values()) {
            if (f.name().equalsIgnoreCase(habitRequest.getFrequency().name())) {
                isValid = true;
                break;
            }
        }
        if (!isValid) {
            throw new BadRequestException(" Frequency '" + habitRequest.getFrequency() +
                    "'Not Match [DAILY,WEEKLY,MONTHLY]");
        }
        return habit;
    }


}
