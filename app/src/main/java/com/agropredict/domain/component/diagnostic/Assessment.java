package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class Assessment {
    private final String severity;
    private final String shortSummary;
    private String recommendationText;

    public Assessment(String severity, String shortSummary) {
        this.severity = severity;
        this.shortSummary = shortSummary;
    }

    public void conclude(String recommendation) {
        this.recommendationText = recommendation;
    }

    public boolean isSevere() {
        return "high".equalsIgnoreCase(severity) || "critical".equalsIgnoreCase(severity);
    }

    public String classify() {
        if (severity == null) return "Analysis complete";
        String normalized = severity.toLowerCase();
        if (normalized.contains("low")) return "Healthy";
        if (normalized.contains("moderate")) return "Moderate issue";
        if (normalized.contains("high")) return "Severe issue";
        return "Analysis complete";
    }

    public void accept(IDiagnosticVisitor visitor) {
        visitor.visitAssessment(severity, shortSummary);
        if (recommendationText != null) visitor.visitRecommendation(recommendationText);
    }
}