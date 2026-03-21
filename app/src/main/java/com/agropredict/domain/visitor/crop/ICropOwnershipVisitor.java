package com.agropredict.domain.visitor.crop;

public interface ICropOwnershipVisitor {
    void visitOwnership(String userIdentifier, String stageIdentifier);
}