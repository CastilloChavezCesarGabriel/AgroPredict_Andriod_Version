package com.agropredict.domain.validation;

public final class PasswordCriteria {
    private boolean hasUppercase;
    private boolean hasLowercase;
    private boolean hasDigit;
    private boolean hasSpecialCharacter;

    public void update(char character) {
        if (Character.isUpperCase(character)) {
            hasUppercase = true;
        }

        if (Character.isLowerCase(character)) {
            hasLowercase = true;
        }

        if (Character.isDigit(character)) {
            hasDigit = true;
        }

        if (!Character.isLetterOrDigit(character)) {
            hasSpecialCharacter = true;
        }
    }

    public boolean isValid() {
        return hasUppercase && hasLowercase && hasDigit && hasSpecialCharacter;
    }
}