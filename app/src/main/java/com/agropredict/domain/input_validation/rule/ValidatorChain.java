package com.agropredict.domain.input_validation.rule;

import com.agropredict.domain.input_validation.ITextValidator;
import com.agropredict.domain.input_validation.gate.IValidationGate;

public abstract class ValidatorChain implements ITextValidator {
    @Override
    public final void check(String text, IValidationGate gate) {
        inspect(text, gate);
        gate.pass();
    }

    protected abstract void inspect(String text, IValidationGate gate);
}
