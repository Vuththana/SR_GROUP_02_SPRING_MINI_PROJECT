package org.goros.habit_tracker.model.response;

import lombok.Data;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.UUID;

@Data
public class AppUserResponse {
    private UUID appUserId;
    private String username;
    private String email;
    private Long level;
    private Long xp;
    private String profileImageUrl;
    private Boolean isVerified;
    private Timestamp createdAt;
}