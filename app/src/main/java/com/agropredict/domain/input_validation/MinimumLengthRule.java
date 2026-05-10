package com.agropredict.domain.input_validation;

public final class MinimumLengthRule extends TextRule {
    private final int minimumLength;

    public MinimumLengthRule(int minimumLength, String reason) {
        super(reason);
        this.minimumLength = minimumLength;
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && text.length() >= minimumLength;
    }
}