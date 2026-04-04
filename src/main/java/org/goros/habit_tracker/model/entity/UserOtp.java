package org.goros.habit_tracker.model.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserOtp {
    private Integer otpId;
    private UUID appUserId;
    private String otpCode;
    private Instant expiresAt;
    private Boolean used;
}
