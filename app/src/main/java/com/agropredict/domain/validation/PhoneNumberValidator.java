package com.agropredict.domain.validation;

public final class PhoneNumberValidator implements ITextValidator {
    private static final int MINIMUM_LENGTH = 7;
    private static final int MAXIMUM_LENGTH = 15;
    private static final String PHONE_PATTERN = "^[0-9]+$";

    @Override
    public boolean isValid(String text) {
        if (text == null || text.isEmpty()) return true;
        if (text.length() < MINIMUM_LENGTH || text.length() > MAXIMUM_LENGTH) return false;
        return text.matches(PHONE_PATTERN);
    }
}