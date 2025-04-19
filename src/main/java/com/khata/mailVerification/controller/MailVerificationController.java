package com.khata.mailVerification.controller;

import com.khata.exceptions.ApiException;
import com.khata.mailVerification.dto.MailVerificationDTO;
import com.khata.mailVerification.service.MailVerificationService;
import com.khata.payload.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
public class MailVerificationController {

    private final MailVerificationService mailVerificationService;

    public MailVerificationController(MailVerificationService mailVerificationService) {
        this.mailVerificationService = mailVerificationService;
    }

    @PostMapping("/verify")
    public ResponseEntity<ApiResponse<Void>> verifyEmail(@RequestBody MailVerificationDTO verificationDTO) {
        try {
            mailVerificationService.verifyOTP(verificationDTO.getEmail(), verificationDTO.getOtp());
            return ResponseEntity.ok(new ApiResponse<>(null, HttpStatus.OK.value(), "Email verified successfully"));
        } catch (ApiException | IllegalArgumentException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Something went wrong"));
        }
    }

    @PostMapping("/resend")
    public ResponseEntity<ApiResponse<Void>> resendVerificationEmail(@RequestBody MailVerificationDTO verificationDTO) {
        try {
            mailVerificationService.sendVerificationEmail(verificationDTO.getEmail());
            return ResponseEntity.ok(new ApiResponse<>(null, HttpStatus.OK.value(), "Verification email sent successfully"));
        } catch (ApiException ex) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>(null, HttpStatus.BAD_REQUEST.value(), ex.getMessage()));
        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse<>(null, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to send verification email"));
        }
    }


}
