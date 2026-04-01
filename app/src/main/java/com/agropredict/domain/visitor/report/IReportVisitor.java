package com.agropredict.domain.visitor.report;

public interface IReportVisitor {
    void visitIdentity(String identifier, String format);
    void visitContext(String diagnosticIdentifier, String cropIdentifier);
    void visitStorage(String userIdentifier, String filePath);
}