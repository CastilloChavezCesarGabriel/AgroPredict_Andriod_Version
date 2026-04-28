package com.agropredict.domain.component.diagnostic;

public final class HighSeverity extends Severity {
    public HighSeverity(String value) {
        super(value);
    }

    @Override
    public void accept(ISeverityVisitor visitor) {
        visitor.visit("Severe issue", 2);
    }
}