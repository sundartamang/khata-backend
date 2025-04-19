package com.khata.mailVerification.repositories;

import com.khata.mailVerification.entity.MailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MailVerificationRepo extends JpaRepository<MailVerification, Long> {
    Optional<MailVerification> findByEmail(String email);
    Optional<MailVerification> findByEmailAndOtp(String email, String otp);
}