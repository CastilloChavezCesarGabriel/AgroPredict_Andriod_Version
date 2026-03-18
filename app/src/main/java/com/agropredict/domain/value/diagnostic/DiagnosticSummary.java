package com.agropredict.domain.value.diagnostic;

import com.agropredict.domain.visitor.IDiagnosticSummaryVisitor;

public final class DiagnosticSummary {
    private final String severity;
    private final String shortSummary;

    public DiagnosticSummary(String severity, String shortSummary) {
        this.severity = severity;
        this.shortSummary = shortSummary;
    }

    public void accept(IDiagnosticSummaryVisitor visitor) {
        visitor.visitSummary(severity, shortSummary);
    }
}