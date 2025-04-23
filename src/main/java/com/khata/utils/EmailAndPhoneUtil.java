package com.khata.utils;

public class EmailAndPhoneUtil {

    public static boolean isValidEmail(String email) {
        return email != null && !email.isEmpty() && email.matches(AppConstants.EMAIL_REGEX);
    }

    public static boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber != null && !phoneNumber.isEmpty() && phoneNumber.matches(AppConstants.PHONE_REGEX);
    }
}
