package com.agropredict.domain.input_validation;

public final class PassphraseRequirementRule extends TextRule {
    public PassphraseRequirementRule(String reason) {
        super(reason);
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && new PassphraseRequirement().accepts(text);
    }
}