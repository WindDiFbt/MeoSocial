package com.web.meosocial.util;

import java.time.LocalDate;

public class ValidationUtils {

    public static String validateUsername(String username) {
        if(username.isEmpty()){
            return "Username cannot be empty.";
        }
        if (username.contains(" ")) {
            return "Username cannot contain spaces.";
        }
        if (username.length() < 5) {
            return "Username must be at least 5 characters long.";
        }
        if (!username.matches("^[\\x00-\\x7F]+$")) {
            return "Username cannot contain special characters or accents.";
        }
        return null;
    }

    public static String validatePassword(String password) {
        if (password.isEmpty()) {
            return "Password cannot be empty.";
        }
        if (password.length() < 8) {
            return "Password must be at least 8 characters long.";
        }
        if (!password.matches(".*[A-Z].*")) {
            return "Password must contain at least one uppercase letter.";
        }
        if (!password.matches(".*\\d.*")) {
            return "Password must contain at least one digit.";
        }

        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "Password must contain at least one special character.";
        }
        return null;
    }

    public static String validateFullName(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }
        if (name.matches(".*\\d.*") || name.matches(".*[^a-zA-Z\\s].*")) {
            return "Invalid name. Must not contain numbers and special characters.";
        }
        return null;
    }

    public static String validateEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return null;
        }
        if (!email.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$")) {
            return "Email contains invalid characters.";
        }
        return null;
    }

    public static String validatePhone(String phone) {
        if (phone == null || phone.trim().isEmpty()) {
            return null;
        }
        if (!phone.matches("^\\d{1,11}$")) {
            return "Invalid phone number";
        }
        return null;
    }


    public static String validateDOB(LocalDate DOB) {
        if (DOB == null) {
            return null;
        }
        if (DOB.isAfter(LocalDate.now())) {
            return "Date of birth cannot be in the future.";
        }
//        if (DOB.getMonthValue() < 1 || DOB.getMonthValue() > 12) {
//            return "Month must be between 1 and 12.";
//        }
//        try {
//            LocalDate.of(DOB.getYear(), DOB.getMonth(), DOB.getDayOfMonth());
//        } catch (DateTimeParseException e) {
//            return "Invalid day for the given month.";
//        }
        return null;
    }
}
