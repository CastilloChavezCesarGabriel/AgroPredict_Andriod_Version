package com.agropredict.domain.visitor.diagnostic;

public interface IDiagnosticContextVisitor {
    void visitContext(String cropIdentifier, String imageIdentifier);
}