package org.goros.habit_tracker.service;

import lombok.RequiredArgsConstructor;
import org.goros.habit_tracker.model.entity.AppUser;
import org.goros.habit_tracker.model.entity.UserOtp;
import org.goros.habit_tracker.repository.AppUserRepository;
import org.goros.habit_tracker.repository.UserOtpRepository;
import org.goros.habit_tracker.utils.OtpGenerator;
import org.springframework.stereotype.Service;


import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OtpService {

    private final UserOtpRepository otpRepo;
    private final EmailService emailService;
    private final AppUserRepository appUserRepository;

    public void generateAndSendOtp(String email) throws Exception {
        String otpCode = OtpGenerator.generateOTP(6);

        AppUser user = appUserRepository.getUserByEmailOrUsername(email);

        if(user != null) {
            UserOtp otp = new UserOtp();
            otp.setAppUserId(user.getAppUserId());
            otp.setOtpCode(otpCode);
            otp.setExpiresAt(Instant.now().plus(5, ChronoUnit.MINUTES));

            otpRepo.saveOtp(otp);

            emailService.sendOtpEmail(user.getEmail(), user.getUsername(), otpCode);
        }
    }

    public boolean verifyOtp(String email, String otpCode) {
        UserOtp otp = otpRepo.validateOtp(email, otpCode);
        if (otp == null) return false;
        appUserRepository.updateUserVerification(email);
        otpRepo.markOtpUsed(otp.getOtpId());
        return true;
    }
}