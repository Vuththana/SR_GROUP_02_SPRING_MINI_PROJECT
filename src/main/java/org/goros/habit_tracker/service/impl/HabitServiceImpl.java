package org.goros.habit_tracker.service.impl;

import java.util.List;
import java.util.UUID;

import org.goros.habit_tracker.exception.BadRequestException;
import org.goros.habit_tracker.exception.NotFoundException;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.enums.Frequency;
import org.goros.habit_tracker.model.request.HabitRequest;
import org.goros.habit_tracker.repository.HabitRepository;
import org.goros.habit_tracker.service.HabitService;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class HabitServiceImpl implements HabitService {
    private final HabitRepository habitRepository;

    @Override
    public List<Habit> getAllHabits(Integer page, Integer size) {
        int offset = (page - 1) * size;
        return habitRepository.getAllHabits(offset, size);
    }

    @Override
    public Habit getHabitById(UUID habitId) {
        Habit habit = habitRepository.getHabitById(habitId);
        if(habit == null) {
            throw new NotFoundException("Habit With ID [" + habitId + "] Not Found!");
        }
        return habit;
    }

    @Override
    public Habit saveHabit(HabitRequest habitRequest) {
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
        Habit habit = habitRepository.saveHabit(habitRequest);
        return habit;
    }
}
