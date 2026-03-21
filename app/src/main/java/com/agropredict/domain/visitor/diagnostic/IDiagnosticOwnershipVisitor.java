package com.agropredict.domain.visitor.diagnostic;

public interface IDiagnosticOwnershipVisitor {
    void visitOwnership(String userIdentifier, String recommendationText);
}