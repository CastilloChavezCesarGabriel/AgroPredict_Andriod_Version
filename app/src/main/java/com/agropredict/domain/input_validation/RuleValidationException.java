package com.agropredict.domain.input_validation;

public final class RuleValidationException extends RuntimeException {
    public RuleValidationException(String reason) {
        super(reason);
    }

    public void announce(IValidationGate gate) {
        gate.fail(getMessage());
    }
}
