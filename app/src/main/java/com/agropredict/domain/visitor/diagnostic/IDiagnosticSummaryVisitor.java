package com.agropredict.domain.visitor.diagnostic;

public interface IDiagnosticSummaryVisitor {
    void visitSummary(String severity, String shortSummary);
}