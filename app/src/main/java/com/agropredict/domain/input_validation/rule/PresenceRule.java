package com.agropredict.domain.input_validation.rule;

import com.agropredict.domain.input_validation.failure.IValidationFailure;

public final class PresenceRule extends TextRule {
    public PresenceRule(IValidationFailure failure) {
        super(failure);
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && !text.isEmpty();
    }
}
