package com.agropredict.domain.input_validation;

import static org.junit.Assert.assertTrue;

public final class ValidatorTester {
    private final ITextValidator validator;

    public ValidatorTester(ITextValidator validator) {
        this.validator = validator;
    }

    public void accepts(String input) {
        CapturingValidationGate gate = new CapturingValidationGate();
        validator.check(input, gate);
        assertTrue(gate.hasPassed());
    }

    public void rejects(String input) {
        CapturingValidationGate gate = new CapturingValidationGate();
        validator.check(input, gate);
        assertTrue(gate.hasFailed());
    }
}
