package com.agropredict.domain.input_validation.gate;

public final class RuleValidationException extends RuntimeException {
    public RuleValidationException(String message) {
        super(message);
    }
}
