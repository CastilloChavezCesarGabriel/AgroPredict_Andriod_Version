package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticAssessmentVisitor;

public final class DiagnosticAssessment {
    private final DiagnosticSummary summary;
    private final DiagnosticOwnership ownership;

    public DiagnosticAssessment(DiagnosticSummary summary, DiagnosticOwnership ownership) {
        this.summary = summary;
        this.ownership = ownership;
    }

    public boolean isSevere() {
        return summary != null && summary.isSevere();
    }

    public void accept(IDiagnosticAssessmentVisitor visitor) {
        visitor.visit(summary, ownership);
    }
}