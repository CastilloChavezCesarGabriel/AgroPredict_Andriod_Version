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

    public static String classify(String severity) {
        if (severity == null) return "Analysis complete";
        String normalized = severity.toLowerCase();
        if (normalized.contains("low")) return "Healthy";
        if (normalized.contains("moderate")) return "Moderate issue";
        if (normalized.contains("high")) return "Severe issue";
        return "Analysis complete";
    }

    public void accept(IDiagnosticSummaryVisitor visitor) {
        visitor.visitSummary(severity, shortSummary);
    }
}