package com.agropredict.domain.validation;

public final class EmailValidator implements ITextValidator {
    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9][a-zA-Z0-9._-]*@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    public boolean validate(String text) {
        if (text == null || text.isEmpty()) return false;
        return text.matches(EMAIL_PATTERN);
    }
}