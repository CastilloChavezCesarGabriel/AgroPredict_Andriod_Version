package com.agropredict.domain.component.diagnostic;

public final class UnknownSeverity extends Severity {
    public UnknownSeverity(String raw) {
        super(raw);
    }

    @Override
    public void accept(ISeverityHandler handler) {
        handler.onUnknown();
    }
}