package com.agropredict.domain.visitor;

public interface IReportContextVisitor {
    void visitReportContext(String diagnosticIdentifier, String cropIdentifier);
}