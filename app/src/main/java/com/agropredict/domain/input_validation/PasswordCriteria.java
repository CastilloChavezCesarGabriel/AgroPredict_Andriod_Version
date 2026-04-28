package com.agropredict.domain.input_validation;

public final class PasswordCriteria {
    public boolean isValid(String password) {
        boolean upper = false;
        boolean lower = false;
        boolean digit = false;
        boolean special = false;
        for (char character : password.toCharArray()) {
            if (Character.isUpperCase(character)) upper = true;
            if (Character.isLowerCase(character)) lower = true;
            if (Character.isDigit(character)) digit = true;
            if (!Character.isLetterOrDigit(character)) special = true;
        }
        return upper && lower && digit && special;
    }
}