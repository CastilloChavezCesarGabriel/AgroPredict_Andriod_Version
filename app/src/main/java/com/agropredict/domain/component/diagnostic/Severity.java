package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public abstract class Severity {
    protected final String raw;

    protected Severity(String raw) {
        this.raw = raw;
    }

    public abstract void accept(ISeverityHandler handler);

    public final void accept(IDiagnosticVisitor visitor, String shortSummary) {
        visitor.visitAssessment(raw, shortSummary);
    }

    public static Severity of(String raw) {
        if (raw == null) return new UnknownSeverity(null);
        String normalized = raw.toLowerCase();
        if (normalized.contains("low")) return new LowSeverity(raw);
        if (normalized.contains("moderate")) return new ModerateSeverity(raw);
        if (normalized.contains("high") || normalized.contains("critical")) return new HighSeverity(raw);
        return new UnknownSeverity(raw);
    }
}