package com.agropredict.domain.input_validation.rule;

import com.agropredict.domain.input_validation.failure.IValidationFailure;

public final class MinimumLengthRule extends TextRule {
    private final int minimumLength;

    public MinimumLengthRule(int minimumLength, IValidationFailure failure) {
        super(failure);
        this.minimumLength = minimumLength;
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && text.length() >= minimumLength;
    }
}
