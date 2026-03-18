package com.agropredict.domain.value.diagnostic;

import com.agropredict.domain.visitor.IDiagnosticContentVisitor;

public final class DiagnosticContent {
    private final DiagnosticConditions conditions;
    private final DiagnosticAssessment assessment;

    public DiagnosticContent(DiagnosticConditions conditions, DiagnosticAssessment assessment) {
        this.conditions = conditions;
        this.assessment = assessment;
    }

    public void accept(IDiagnosticContentVisitor visitor) {
        visitor.visit(conditions, assessment);
    }
}