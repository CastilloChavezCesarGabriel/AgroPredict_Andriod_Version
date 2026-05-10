package com.agropredict.domain.input_validation;

import com.agropredict.domain.guard.ArgumentPrecondition;

public abstract class TextRule implements ITextValidator {
    private final String reason;

    protected TextRule(String reason) {
        this.reason = ArgumentPrecondition.validate(reason, "text rule reason");
    }

    @Override
    public final void check(String text, IValidationGate gate) {
        if (accepts(text)) {
            gate.pass();
        } else {
            gate.fail(reason);
        }
    }

    protected abstract boolean accepts(String text);
}