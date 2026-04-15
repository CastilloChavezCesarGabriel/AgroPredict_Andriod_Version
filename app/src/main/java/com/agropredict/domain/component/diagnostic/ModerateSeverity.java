package com.agropredict.domain.component.diagnostic;

public final class ModerateSeverity extends Severity {
    public ModerateSeverity(String raw) {
        super(raw);
    }

    @Override
    public void accept(ISeverityHandler handler) {
        handler.onModerate();
    }
}