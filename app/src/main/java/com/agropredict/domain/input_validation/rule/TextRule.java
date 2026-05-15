package com.agropredict.domain.input_validation.rule;

import com.agropredict.domain.input_validation.ITextValidator;
import com.agropredict.domain.input_validation.failure.IValidationFailure;
import com.agropredict.domain.input_validation.gate.IValidationGate;

import java.util.Objects;

public abstract class TextRule implements ITextValidator {
    private final IValidationFailure failure;

    protected TextRule(IValidationFailure failure) {
        this.failure = Objects.requireNonNull(failure, "text rule requires a failure");
    }

    @Override
    public final void check(String text, IValidationGate gate) {
        if (accepts(text)) {
            gate.pass();
        } else {
            gate.fail(failure);
        }
    }

    protected abstract boolean accepts(String text);
}
