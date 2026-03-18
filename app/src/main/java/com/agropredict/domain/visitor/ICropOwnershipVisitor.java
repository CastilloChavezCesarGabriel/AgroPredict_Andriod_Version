package com.agropredict.domain.visitor;

public interface ICropOwnershipVisitor {
    void visitOwnership(String userIdentifier, String stageIdentifier);
}