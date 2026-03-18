package com.agropredict.domain.validation;

import java.util.regex.Pattern;

public final class FullNameValidator implements ITextValidator {
    private static final int MINIMUM_LENGTH = 2;
    private static final int MAXIMUM_LENGTH = 50;

    private static final Pattern NAME_PATTERN = Pattern.compile("^(?=.*\\p{L})[\\p{L}\\s]+$");

    @Override
    public boolean validate(String text) {
        if (text == null) return false;

        String trimmed = text.trim();

        if (trimmed.length() < MINIMUM_LENGTH || trimmed.length() > MAXIMUM_LENGTH) {
            return false;
        }

        return NAME_PATTERN.matcher(trimmed).matches();
    }
}