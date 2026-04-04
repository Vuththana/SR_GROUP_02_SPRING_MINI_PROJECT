package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.request.ProfileUpdateRequest;
import org.goros.habit_tracker.model.response.ProfileResponse;
import org.goros.habit_tracker.repository.ProfileRepository;
import org.goros.habit_tracker.service.ProfileService;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private static final String UNAUTHORIZED_MESSAGE = "Authentication required or token is invalid";

    private final ProfileRepository profileRepository;

    @Override
    public ProfileResponse getProfile() {
        UUID appUserId = extractAuthenticatedUserId();
        ProfileResponse profile = profileRepository.findById(appUserId);
        if (profile == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }
        return profile;
    }

    @Override
    public ProfileResponse updateProfile(ProfileUpdateRequest request) {
        UUID appUserId = extractAuthenticatedUserId();
        int updated = profileRepository.updateById(appUserId, request);
        if (updated == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }
        return profileRepository.findById(appUserId);
    }

    @Override
    public void deleteProfile() {
        UUID appUserId = extractAuthenticatedUserId();
        int deleted = profileRepository.deleteById(appUserId);
        if (deleted == 0) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Profile not found");
        }
    }

    private UUID extractAuthenticatedUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
        }

        Object principal = authentication.getPrincipal();

        if (principal instanceof AppUser appUser && appUser.getAppUserId() != null) {
            return appUser.getAppUserId();
        }

        if (principal instanceof String principalValue) {
            try {
                return UUID.fromString(principalValue);
            } catch (IllegalArgumentException ignored) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
            }
        }

        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, UNAUTHORIZED_MESSAGE);
    }
}