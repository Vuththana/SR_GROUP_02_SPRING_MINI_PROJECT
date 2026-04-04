package org.goros.habit_tracker.model.request;

public record ProfileUpdateRequest(
        String username,
        String profileImageUrl
) {
}