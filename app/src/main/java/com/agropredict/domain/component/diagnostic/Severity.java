package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public abstract class Severity {
    protected final String value;

    protected Severity(String value) {
        this.value = value;
    }

    public abstract void accept(ISeverityVisitor visitor);

    public final void accept(IDiagnosticVisitor visitor, String shortSummary) {
        visitor.visitAssessment(value, shortSummary);
    }

    public static Severity classify(String value) {
        if (value == null) return new UnknownSeverity(null);
        String normalized = value.toLowerCase();
        if (normalized.contains("low")) return new LowSeverity(value);
        if (normalized.contains("moderate")) return new ModerateSeverity(value);
        if (normalized.contains("high") || normalized.contains("critical")) return new HighSeverity(value);
        return new UnknownSeverity(value);
    }
}