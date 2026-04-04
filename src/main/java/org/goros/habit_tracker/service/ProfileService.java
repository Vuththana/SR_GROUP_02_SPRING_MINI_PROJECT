package org.goros.habit_tracker.service;

import org.goros.habit_tracker.model.request.ProfileUpdateRequest;
import org.goros.habit_tracker.model.response.ProfileResponse;

public interface ProfileService {
    ProfileResponse getProfile();
    ProfileResponse updateProfile(ProfileUpdateRequest request);
    void deleteProfile();
}