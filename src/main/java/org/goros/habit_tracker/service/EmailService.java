package org.goros.habit_tracker.service;

import jakarta.mail.MessagingException;

public interface EmailService {
     void sendOtpEmail(String toEmail, String name, String otp) throws MessagingException;
}
