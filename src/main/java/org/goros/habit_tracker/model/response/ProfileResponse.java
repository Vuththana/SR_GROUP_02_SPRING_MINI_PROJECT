package org.goros.habit_tracker.model.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record ProfileResponse(
        UUID appUserId,
        String username,
        String email,
        Integer level,
        Integer xp,
        String profileImageUrl,
        Boolean isVerified,
        LocalDateTime createdAt

) {

}