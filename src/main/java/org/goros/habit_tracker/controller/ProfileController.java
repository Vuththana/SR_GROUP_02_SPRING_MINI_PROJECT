package org.goros.habit_tracker.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.request.ProfileUpdateRequest;
import org.goros.habit_tracker.model.response.ApiResponse;
import org.goros.habit_tracker.model.response.ApiResponseVoid;
import org.goros.habit_tracker.model.response.ProfileResponse;
import org.goros.habit_tracker.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;

@RestController
@RequestMapping("/api/v1/profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "basicAuth")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> getProfile() {
        ProfileResponse payload = profileService.getProfile();
        ApiResponse<ProfileResponse> response = ApiResponse.<ProfileResponse>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Profile fetched successfully")
                .payload(payload)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @PutMapping
    public ResponseEntity<ApiResponse<ProfileResponse>> updateProfile(@RequestBody ProfileUpdateRequest requestBody) {
        ProfileResponse payload = profileService.updateProfile(requestBody);
        ApiResponse<ProfileResponse> response = ApiResponse.<ProfileResponse>builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Profile updated successfully")
                .payload(payload)
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping
    public ResponseEntity<ApiResponseVoid> deleteProfile() {
        profileService.deleteProfile();
        ApiResponseVoid response = ApiResponseVoid.builder()
                .success(true)
                .status(HttpStatus.OK)
                .message("Profile deleted successfully")
                .timestamp(Instant.now())
                .build();
        return ResponseEntity.ok(response);
    }
}