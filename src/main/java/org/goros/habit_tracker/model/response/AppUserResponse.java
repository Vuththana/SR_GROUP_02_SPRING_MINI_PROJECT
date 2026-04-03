package org.goros.habit_tracker.model.response;

import lombok.Data;

import java.time.Instant;

@Data
public class AppUserResponse {
    private Long appUserId;
    private String username;
    private String email;
    private Long level;
    private Long xp;
    private String profileImageUrl;
    private Boolean isVerified;
    private Instant createdAt;
}