package com.agropredict.domain.visitor.diagnostic;

public interface IDiagnosticPairVisitor {
    void match(String diagnosticIdentifier, String otherIdentifier);
}