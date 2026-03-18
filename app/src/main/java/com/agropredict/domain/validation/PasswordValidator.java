package com.agropredict.domain.validation;

public final class PasswordValidator implements ITextValidator {
    private static final int MINIMUM_LENGTH = 8;

    @Override
    public boolean validate(String text) {
        if (isNullOrTooShort(text)) return false;

        PasswordCriteria passwordCriteria = new PasswordCriteria();
        for (char character : text.toCharArray()) {
            passwordCriteria.update(character);
            if (passwordCriteria.isValid()) {
                return true;
            }
        }

        return passwordCriteria.isValid();
    }

    private boolean isNullOrTooShort(String text) {
        return text == null || text.length() < MINIMUM_LENGTH;
    }
}