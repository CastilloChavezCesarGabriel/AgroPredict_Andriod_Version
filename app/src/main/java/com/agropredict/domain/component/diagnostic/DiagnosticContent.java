package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticContentVisitor;

public final class DiagnosticContent {
    private final DiagnosticConditions conditions;
    private final DiagnosticAssessment assessment;

    public DiagnosticContent(DiagnosticConditions conditions, DiagnosticAssessment assessment) {
        this.conditions = conditions;
        this.assessment = assessment;
    }

    public boolean isSevere() {
        return assessment != null && assessment.isSevere();
    }

    public void accept(IDiagnosticContentVisitor visitor) {
        visitor.visit(conditions, assessment);
    }
}