package com.agropredict.domain.component.diagnostic;

public final class HighSeverity extends Severity {
    public HighSeverity(String raw) {
        super(raw);
    }

    @Override
    public void accept(ISeverityHandler handler) {
        handler.onSevere();
    }
}