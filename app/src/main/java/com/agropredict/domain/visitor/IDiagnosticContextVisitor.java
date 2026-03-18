package com.agropredict.domain.visitor;

public interface IDiagnosticContextVisitor {
    void visitContext(String cropIdentifier, String imageIdentifier);
}