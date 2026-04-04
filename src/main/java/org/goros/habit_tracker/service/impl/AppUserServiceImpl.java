package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.exception.ConflictException;
import org.goros.habit_tracker.exception.UserNotVerifiedException;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.request.AppUserRequest;
import org.goros.habit_tracker.model.response.AppUserResponse;
import org.goros.habit_tracker.repository.AppUserRepository;
import org.goros.habit_tracker.service.AppUserService;
import org.goros.habit_tracker.service.RedisUserCacheService;
import org.jspecify.annotations.NullMarked;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@NullMarked
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final RedisUserCacheService redisUserCacheService;
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
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

        if(appUserRepository.getUserByEmailOrUsername(request.getEmail()) != null) {
            throw new ConflictException("This email already exists.");
        }

        if(appUserRepository.getUserByEmailOrUsername(request.getUsername()) != null) {
            throw new ConflictException("This username already exists");
        }

        AppUser appUser = modelMapper.map(request, AppUser.class);
        appUser.setAppUserId(UUID.randomUUID());
        appUser.setIsVerified(false);
        appUser.setXp(0L);
        appUser.setLevel(0L);
        appUser.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        String verificationKey = "verify:" + appUser.getEmail();
        redisUserCacheService.saveUser(verificationKey, appUser, 15);

        return modelMapper.map(appUser, AppUserResponse.class);
    }
}
