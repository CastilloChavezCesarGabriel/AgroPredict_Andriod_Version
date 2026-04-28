package com.agropredict.domain.input_validation;

public final class PasswordValidator implements ITextValidator {
    private static final int MINIMUM_LENGTH = 8;

    @Override
    public boolean isValid(String text) {
        if (text == null || text.length() < MINIMUM_LENGTH) return false;
        return new PasswordCriteria().isValid(text);
    }
}