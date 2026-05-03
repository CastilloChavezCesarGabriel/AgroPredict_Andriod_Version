package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class Assessment {
    private final Severity severity;
    private final Recommendation recommendation;

    public Assessment(String severity, Recommendation recommendation) {
        this.severity = Severity.classify(severity);
        this.recommendation = recommendation;
    }

    public void inspect(ISeverityVisitor visitor) {
        severity.accept(visitor);
    }

    public void accept(IDiagnosticVisitor visitor) {
        severity.accept(visitor);
        if (recommendation != null) recommendation.accept(visitor);
    }
}
