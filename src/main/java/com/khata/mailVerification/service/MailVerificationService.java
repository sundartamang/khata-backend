package com.khata.mailVerification.service;

public interface MailVerificationService {
    void sendVerificationEmail(String email);

    void verifyOTP(String email, String otp);
}
