package org.goros.habit_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.entity.HabitLog;
import org.goros.habit_tracker.model.request.HabitLogRequest;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.service.HabitLogService;
import org.goros.habit_tracker.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    @Operation(summary = "Get all habit logs by habit ID")
    public ResponseEntity<ApiResponse<HabitLog>> getHabitLogByHabitId(@PathVariable("habit-id") UUID habitId) {
        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HabitLog habitLog = habitLogService.getHabitLogByHabitId(habitId, currentUser.getAppUserId());
        ApiResponse<HabitLog> response = ResponseUtil.success(HttpStatus.OK, "Habit Log successfully fetched", habitLog);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping
    @Operation(summary = "Create a new habit log")
    public ResponseEntity<ApiResponse<HabitLog>> saveHabitLog(@Valid  @RequestBody HabitLogRequest habitLogRequest) {
        AppUser currentUser = (AppUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        HabitLog habitLog = habitLogService.saveHabitLog(habitLogRequest, currentUser.getAppUserId());
        ApiResponse<HabitLog> response = ResponseUtil.success(HttpStatus.CREATED, "Habit Log successfully created", habitLog);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
