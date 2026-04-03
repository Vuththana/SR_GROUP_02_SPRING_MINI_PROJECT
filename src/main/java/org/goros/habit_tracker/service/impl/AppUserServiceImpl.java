package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.repository.AppUserRepository;
import org.goros.habit_tracker.service.AppUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {
    private final AppUserRepository appUserRepository;
    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        return appUserRepository.getUserByEmailOrUsername(identifier);
    }
}
