package com.agropredict.domain.input_validation;

public final class EmailValidator implements ITextValidator {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9]([a-zA-Z0-9_-]*(\\.[a-zA-Z0-9_-]+)*)@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public boolean isValid(String text) {
        if (text == null || text.isEmpty()) return false;
        return text.matches(EMAIL_PATTERN);
    }
}
