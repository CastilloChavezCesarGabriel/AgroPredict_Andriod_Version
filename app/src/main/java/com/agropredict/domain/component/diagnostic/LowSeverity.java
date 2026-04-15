package com.agropredict.domain.component.diagnostic;

public final class LowSeverity extends Severity {
    public LowSeverity(String raw) {
        super(raw);
    }

    @Override
    public void accept(ISeverityHandler handler) {
        handler.onHealthy();
    }
}