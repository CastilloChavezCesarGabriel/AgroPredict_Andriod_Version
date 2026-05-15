package com.agropredict.domain.input_validation.rule;

import com.agropredict.domain.input_validation.requirement.PassphraseRequirement;
import com.agropredict.domain.input_validation.failure.IValidationFailure;

public final class PassphraseRequirementRule extends TextRule {
    private final PassphraseRequirement requirement;

    public PassphraseRequirementRule(IValidationFailure failure) {
        super(failure);
        this.requirement = new PassphraseRequirement();
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && requirement.accepts(text);
    }
}
