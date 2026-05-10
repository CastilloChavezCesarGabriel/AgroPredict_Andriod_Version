package com.agropredict.domain.input_validation;

public abstract class ValidatorChain implements ITextValidator {
    @Override
    public final void check(String text, IValidationGate gate) {
        try {
            ValidationGate internal = new ValidationGate();
            inspect(text, internal);
            gate.pass();
        } catch (RuleValidationException invalid) {
            invalid.announce(gate);
        }
    }

    protected abstract void inspect(String text, IValidationGate gate);
}