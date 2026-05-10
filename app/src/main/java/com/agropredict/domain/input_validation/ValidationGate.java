package com.agropredict.domain.input_validation;

public final class ValidationGate implements IValidationGate {
    @Override
    public void pass() {}

    @Override
    public void fail(String reason) {
        throw new RuleValidationException(reason);
    }
}