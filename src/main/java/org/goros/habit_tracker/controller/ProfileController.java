package org.goros.habit_tracker.controller;

import org.goros.habit_tracker.model.entity.Profile;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.utils.ResponseUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class ProfileController {

    @GetMapping
    public ResponseEntity<ApiResponse<Profile>> getProfile() {
        ApiResponse response = ResponseUtil.success(null, "Profile fetched successfully", null);
        return ResponseEntity.ok(response);
    }
}
