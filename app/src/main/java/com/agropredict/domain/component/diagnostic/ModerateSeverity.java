package com.agropredict.domain.component.diagnostic;

public final class ModerateSeverity extends Severity {
    public ModerateSeverity(String value) {
        super(value);
    }

    @Override
    public void accept(ISeverityVisitor visitor) {
        visitor.visit("Moderate issue", 1);
    }
}