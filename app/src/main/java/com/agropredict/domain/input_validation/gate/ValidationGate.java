package com.agropredict.domain.input_validation.gate;

import com.agropredict.domain.input_validation.failure.IValidationFailure;

public final class ValidationGate implements IValidationGate {
    @Override
    public void pass() {}

    @Override
    public void fail(IValidationFailure failure) {
        failure.encode(text -> { throw new RuleValidationException(text); });
    }
}
