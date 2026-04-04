package org.goros.habit_tracker.service;

import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.request.AppUserRequest;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface AppUserService extends UserDetailsService {

    AppUserResponse register(AppUserRequest request);
}
