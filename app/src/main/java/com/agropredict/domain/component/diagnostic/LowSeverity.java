package com.agropredict.domain.component.diagnostic;

public final class LowSeverity extends Severity {
    public LowSeverity(String value) {
        super(value);
    }

    @Override
    public void accept(ISeverityVisitor visitor) {
        visitor.visit("Healthy", 0);
    }
}