package com.agropredict.domain.value.diagnostic;

import com.agropredict.domain.visitor.IDiagnosticAssessmentVisitor;

public final class DiagnosticAssessment {
    private final DiagnosticSummary summary;
    private final DiagnosticOwnership ownership;

    public DiagnosticAssessment(DiagnosticSummary summary, DiagnosticOwnership ownership) {
        this.summary = summary;
        this.ownership = ownership;
    }

    public void accept(IDiagnosticAssessmentVisitor visitor) {
        visitor.visit(summary, ownership);
    }
}