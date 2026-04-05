package org.goros.habit_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.entity.Habit;
import org.goros.habit_tracker.model.request.HabitRequest;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.model.response.ApiResponseVoid;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.goros.habit_tracker.service.HabitService;
import org.goros.habit_tracker.utils.ResponseUtil;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/habits")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class HabitController {
    private final HabitService habitService;
    private final ModelMapper modelMapper;

    @GetMapping
    @Operation(summary = "Get All Habits")
    public ResponseEntity<ApiResponse<List<Habit>>> getAllHabits(@Positive(message = "Page must be positive") @RequestParam(defaultValue = "1") Integer page, @Positive(message = "Size must be positive") @RequestParam(defaultValue = "10") Integer size ) {
        AppUser currentAppUser = (AppUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        UUID currentAppUserId = currentAppUser.getAppUserId();
        List<Habit> habits = habitService.getAllHabits(page, size, currentAppUserId);
        ApiResponse<List<Habit>> response = ResponseUtil.success( HttpStatus.OK,"Habits successfully fetched", habits);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{habit-id}")
    @Operation(summary = "Get habit by ID")
    public ResponseEntity<ApiResponse<Habit>> getHabitById(@PathVariable("habit-id") UUID habitId) {
        AppUser currentAppUser = (AppUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        UUID currentAppUserId = currentAppUser.getAppUserId();
        Habit habit = habitService.getHabitById(habitId , currentAppUserId);
        ApiResponse<Habit> response = ResponseUtil.success(HttpStatus.OK, "Habit successfully fetched", habit);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PostMapping
    @Operation(summary = "Create a new habit")
    public ResponseEntity<ApiResponse<Habit>> saveHabit(@Valid @RequestBody HabitRequest habitRequest) {
        AppUser appUser = (AppUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        AppUserResponse currentUser = modelMapper.map(appUser, AppUserResponse.class);
        Habit habit = habitService.saveHabit(habitRequest, currentUser);
        ApiResponse<Habit> response = ResponseUtil.success(HttpStatus.CREATED,"Habit successfully created", habit);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{habit-id}")
    @Operation(summary = "Delete user profile")
    public ResponseEntity<ApiResponseVoid> deleteHabitById(@PathVariable("habit-id") UUID habitId){
        AppUser currentAppUser = (AppUser) SecurityContextHolder
                .getContext()
                        .getAuthentication()
                                .getPrincipal();
        UUID currentAppUserId = currentAppUser.getAppUserId();
        habitService.deleteHabitById(habitId , currentAppUserId);
        ApiResponseVoid response = ResponseUtil.successVoid(HttpStatus.OK, "Habit successfully Deleted");
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{habit-id}")
    @Operation(summary = "Update user profile")
    public ResponseEntity<ApiResponse<Habit>> updateHabitById(@PathVariable("habit-id") UUID habitId , HabitRequest habitRequest) {
        AppUser currentAppUser = (AppUser) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();
        UUID currentAppUserId = currentAppUser.getAppUserId();
        Habit habit = habitService.updateHabitById(habitId , habitRequest , currentAppUserId);
        ApiResponse<Habit> response = ResponseUtil.success(HttpStatus.OK, "Habit successfully Updated" , habit);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

}
