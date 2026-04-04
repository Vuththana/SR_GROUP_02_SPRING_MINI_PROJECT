package org.goros.habit_tracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.request.HabitRequest;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.service.HabitService;
import org.goros.habit_tracker.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/habits")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class HabitController {
    private final HabitService habitService;

    @GetMapping
    public ResponseEntity<ApiResponse<List<Habit>>> getAllHabits(@Positive(message = "Page must be positive") @RequestParam(defaultValue = "1") Integer page, @Positive(message = "Size must be positive") @RequestParam(defaultValue = "10") Integer size) {
        List<Habit> habits = habitService.getAllHabits(page, size);
        ApiResponse<List<Habit>> response = ResponseUtil.success( HttpStatus.OK,"Habits successfully fetched", habits);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{habit-id}")
    public ResponseEntity<ApiResponse<Habit>> getHabitById(@PathVariable("habit-id") UUID habitId) {
        Habit habit = habitService.getHabitById(habitId);
        ApiResponse<Habit> response = ResponseUtil.success(HttpStatus.OK, "Habit successfully fetched", habit);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Habit>> saveEvent(@Valid @RequestBody HabitRequest habitRequest) {
        Habit habit = habitService.saveHabit(habitRequest);
        ApiResponse<Habit> response = ResponseUtil.success(HttpStatus.CREATED,"Habit successfully created", habit);
        return ResponseEntity.status(response.getStatus()).body(response);
    }



}
