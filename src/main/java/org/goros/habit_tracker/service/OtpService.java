package org.goros.habit_tracker.service;

public interface OtpService {
    void generateAndSendOtp(String email, String verificationKey) throws Exception;
    boolean verifyOtp(String email, String otpCode);
}