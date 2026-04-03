package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.exception.UserNotVerifiedException;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.request.AppUserRequest;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.goros.habit_tracker.repository.AppUserRepository;
import org.goros.habit_tracker.service.AppUserService;
import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@NullMarked
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        System.out.println("Looking up user by identifier: " + identifier);
        AppUser user = appUserRepository.getUserByEmailOrUsername(identifier);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with: " + identifier);
        }
        if(appUserRepository.isUserVerified(user.getAppUserId())) {
            throw new UserNotVerifiedException("Your email address is not verified. Please verify your email before logging in.");
        }
        return user;
    }

    @Override
    public AppUserResponse register(AppUserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        AppUser appUser = appUserRepository.register(request);
        return modelMapper.map(appUserRepository.getUserById(appUser.getAppUserId()), AppUserResponse.class);
    }


    // This is for testing (will be removed soon)
    @Override
    public AppUser getUserById(UUID appUserId) {
        return appUserRepository.getUserById(appUserId);
    }
}
