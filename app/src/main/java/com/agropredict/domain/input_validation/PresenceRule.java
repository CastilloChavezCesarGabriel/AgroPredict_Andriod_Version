package com.agropredict.domain.input_validation;

public final class PresenceRule extends TextRule {
    public PresenceRule(String reason) {
        super(reason);
    }

    @Override
    protected boolean accepts(String text) {
        return text != null && !text.isEmpty();
    }
}