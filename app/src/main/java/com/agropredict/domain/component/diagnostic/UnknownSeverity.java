package com.agropredict.domain.component.diagnostic;

public final class UnknownSeverity extends Severity {
    public UnknownSeverity(String value) {
        super(value);
    }

    @Override
    public void accept(ISeverityVisitor visitor) {
        visitor.visit("Analysis complete", 0);
    }
}