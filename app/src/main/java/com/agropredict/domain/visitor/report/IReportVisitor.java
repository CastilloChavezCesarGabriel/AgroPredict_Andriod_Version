package com.agropredict.domain.visitor.report;

public interface IReportVisitor {
    default void visitIdentity(String identifier, String format) {}
    default void visitContext(String diagnosticIdentifier, String cropIdentifier) {}
    default void visitStorage(String userIdentifier, String filePath) {}
}