package com.khata.mailVerification.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class MailVerificationDTO {
    private String email;
    private String otp;
}
