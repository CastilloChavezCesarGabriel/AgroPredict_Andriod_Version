package com.agropredict.domain.visitor;

public interface IDiagnosticSummaryVisitor {
    void visitSummary(String severity, String shortSummary);
}