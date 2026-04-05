package org.goros.habit_tracker.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.Achievement;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.service.AchievementService;
import org.goros.habit_tracker.utils.ResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/achievements")
@SecurityRequirement(name = "basicAuth")
@Validated
public class AchievementController {

    private final AchievementService achievementService;

    @GetMapping
    @Operation(summary = "Get all achievements")
    public ResponseEntity<ApiResponse<List<Achievement>>> getAllAchievement(@Positive @RequestParam(defaultValue = "1") Long page, @Positive @RequestParam(defaultValue = "10") Long size) {
        return ResponseEntity.ok(ResponseUtil.success(HttpStatus.OK, "successfully retrieved achievement ", achievementService.getAllAchievements(page, size)));
    }

    @GetMapping("/app-users")
    @Operation(summary = "Get achievements by App User ID")
    public ResponseEntity<ApiResponse<List<Achievement>>> getAllAchievementByAppUserId(@Positive @RequestParam(defaultValue = "1") Long page, @Positive @RequestParam(defaultValue = "10") Long size) {
        return ResponseEntity.ok(ResponseUtil.success(HttpStatus.OK, "successfully retrieved achievement ", achievementService.getAllAchievementByAppUserId(page, size)));
    }
}
