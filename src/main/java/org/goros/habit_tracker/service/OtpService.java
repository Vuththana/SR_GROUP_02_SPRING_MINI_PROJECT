package org.goros.habit_tracker.service;

public interface OtpService {
    void generateAndSendOtp(String email) throws Exception;
    boolean verifyOtp(String email, String otpCode);
}