package org.goros.habit_tracker.service.impl;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.entity.UserOtp;
import org.goros.habit_tracker.repository.AppUserRepository;
import org.goros.habit_tracker.repository.UserOtpRepository;
import org.goros.habit_tracker.service.EmailService;
import org.goros.habit_tracker.service.OtpService;
import org.goros.habit_tracker.utils.OtpGenerator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
public class OtpServiceImpl implements OtpService {

    private final UserOtpRepository otpRepo;
    private final EmailService emailService;
    private final AppUserRepository appUserRepository;

    @Override
    @Async
    public void generateAndSendOtp(String email) throws Exception {
        String otpCode = OtpGenerator.generateOTP(6);

        AppUser user = appUserRepository.getUserByEmailOrUsername(email);
        System.out.println("User found: " + user);
        System.out.println("Email: " + email);
        if(user != null) {
            UserOtp otp = new UserOtp();
            otp.setAppUserId(user.getAppUserId());
            otp.setOtpCode(otpCode);
            otp.setExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES));

            otpRepo.saveOtp(otp);

            emailService.sendOtpEmail(user.getEmail(), user.getUsername(), otpCode);
        }
    }

    @Override
    public boolean verifyOtp(String email, String otpCode) {
        AppUser appUser = appUserRepository.getUserByEmailOrUsername(email);
        System.out.println(appUser);
        if(appUser == null) {
            throw new UsernameNotFoundException("The email address provided is not registered. Please check and try again");
        }
        UserOtp otp = otpRepo.validateOtp(email, otpCode);
        if (otp == null) return false;
        appUserRepository.updateUserVerification(email);
        otpRepo.markOtpUsed(otp.getOtpId());
        return true;
    }
}