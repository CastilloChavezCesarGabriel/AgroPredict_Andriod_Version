package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticSummaryVisitor;

public final class DiagnosticSummary {
    private final String severity;
    private final String shortSummary;

    public DiagnosticSummary(String severity, String shortSummary) {
        this.severity = severity;
        this.shortSummary = shortSummary;
    }

    public boolean isSevere() {
        return "high".equalsIgnoreCase(severity) || "critical".equalsIgnoreCase(severity);
    }

    public boolean isActionable() {
        return shortSummary != null && !shortSummary.isEmpty();
    }

    public void accept(IDiagnosticSummaryVisitor visitor) {
        visitor.visitSummary(severity, shortSummary);
    }
}