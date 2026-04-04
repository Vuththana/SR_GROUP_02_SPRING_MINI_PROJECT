package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.request.AppUserRequest;
import org.goros.habit_tracker.repository.AppUserRepository;
import org.goros.habit_tracker.service.EmailService;
import org.goros.habit_tracker.service.OtpService;
import org.goros.habit_tracker.service.RedisUserCacheService;
import org.goros.habit_tracker.utils.OtpGenerator;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final EmailService emailService;
    private final AppUserRepository appUserRepository;
    private final RedisUserCacheService redisUserCacheService;

    @Override
    @Async
    public void generateAndSendOtp(String email, String verificationKey) throws Exception {
        String otpCode = OtpGenerator.generateOTP(6);
        String otpKey = "otp:" + verificationKey;

        redisTemplate.opsForValue().set(otpKey, otpCode, Duration.ofMinutes(5));

        AppUser cachedUser = redisUserCacheService.getUser(verificationKey);
        if (cachedUser != null) {
            emailService.sendOtpEmail(cachedUser.getEmail(), cachedUser.getUsername(), otpCode);
        } else {
            AppUser user = appUserRepository.getUserByEmailOrUsername(email);
            if (user != null) {
                emailService.sendOtpEmail(user.getEmail(), user.getUsername(), otpCode);
            } else {
                throw new RuntimeException("User not found for OTP generation");
            }
        }

    }

    @Override
    public boolean verifyOtp(String verificationKey, String otpCode) {
        String otpKey = "otp:verify:" + verificationKey;
        String cachedOtp = (String) redisTemplate.opsForValue().get(otpKey);
        if (!otpCode.equals(cachedOtp)) return false;
        AppUser cachedUser = redisUserCacheService.getUser("verify:"+verificationKey);
        if (cachedUser == null) {
            return false;
        }
        cachedUser.setIsVerified(true);
        AppUserRequest request = new AppUserRequest();
        request.setUsername(cachedUser.getUsername());
        request.setEmail(cachedUser.getEmail());
        request.setPassword(cachedUser.getPassword());
        request.setProfileImageUrl(cachedUser.getProfileImageUrl());

        appUserRepository.registerWithUuid(cachedUser.getAppUserId(), request);
        redisTemplate.delete(otpKey);
        redisUserCacheService.deleteUser(verificationKey);
        return true;
    }
}