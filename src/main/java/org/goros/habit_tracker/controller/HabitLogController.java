package org.goros.habit_tracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.entity.HabitLog;
import org.goros.habit_tracker.model.request.HabitLogRequest;
import org.goros.habit_tracker.repository.HabitLogRepository;
import org.goros.habit_tracker.service.HabitLogService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("api/v1/habit-logs/")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class HabitLogController {
    private final HabitLogService habitLogService;

    @GetMapping("{habit-id}")
    public HabitLog getHabitLogByHabitId(@PathVariable("habit-id") UUID habitId) {
        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return habitLogService.getHabitLogByHabitId(habitId, currentUser.getAppUserId());
    }

    @PostMapping
    public HabitLog saveHabitLog(@Valid  @RequestBody HabitLogRequest habitLogRequest) {
        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HabitLog habitLog = habitLogService.saveHabitLog(habitLogRequest, currentUser.getAppUserId());
        return habitLog;
    }
}
