package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class Assessment {
    private final Severity severity;
    private final String shortSummary;
    private final String recommendationText;

    public Assessment(String severity, String shortSummary, String recommendationText) {
        this.severity = Severity.classify(severity);
        this.shortSummary = shortSummary;
        this.recommendationText = recommendationText;
    }

    public void inspect(ISeverityVisitor visitor) {
        severity.accept(visitor);
    }

    public void accept(IDiagnosticVisitor visitor) {
        severity.accept(visitor, shortSummary);
        if (recommendationText != null) {
            visitor.visitRecommendation(recommendationText);
        }
    }
}