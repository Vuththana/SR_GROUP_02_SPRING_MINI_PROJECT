package org.goros.habit_tracker.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProfileResponse {
    private UUID appUserId;
    private String username;
    private String email;
    private Long level;
    private Integer xp;
    private String profileImageUrl;
    private Boolean isVerified;
    private Timestamp createdAt;
}