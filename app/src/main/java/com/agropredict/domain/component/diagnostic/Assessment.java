package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class Assessment {
    private final Severity severity;
    private final String shortSummary;
    private String recommendationText;

    public Assessment(String severity, String shortSummary) {
        this.severity = Severity.of(severity);
        this.shortSummary = shortSummary;
    }

    public void conclude(String recommendation) {
        this.recommendationText = recommendation;
    }

    public void inspect(ISeverityHandler handler) {
        severity.accept(handler);
    }

    public void accept(IDiagnosticVisitor visitor) {
        severity.accept(visitor, shortSummary);
        if (recommendationText != null) visitor.visitRecommendation(recommendationText);
    }
}