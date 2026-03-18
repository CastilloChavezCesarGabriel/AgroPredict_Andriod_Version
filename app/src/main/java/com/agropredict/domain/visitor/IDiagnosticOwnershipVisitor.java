package com.agropredict.domain.visitor;

public interface IDiagnosticOwnershipVisitor {
    void visitOwnership(String userIdentifier, String recommendationText);
}