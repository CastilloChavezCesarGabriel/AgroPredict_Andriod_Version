package com.agropredict.domain.visitor.report;

public interface IReportContextVisitor {
    void visitContext(String diagnosticIdentifier, String cropIdentifier);
}