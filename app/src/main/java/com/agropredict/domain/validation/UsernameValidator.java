package com.agropredict.domain.validation;

public final class UsernameValidator implements ITextValidator {
    private static final int MINIMUM_LENGTH = 5;
    private static final int MAXIMUM_LENGTH = 32;
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9_]+$";

    @Override
    public boolean isValid(String text) {
        if (text == null) return false;
        if (text.length() < MINIMUM_LENGTH || text.length() > MAXIMUM_LENGTH) return false;
        if (!text.matches(USERNAME_PATTERN)) return false;
        for (int index = 0; index < text.length(); index++) {
            if (Character.isLetter(text.charAt(index))) return true;
        }
        return false;
    }
}