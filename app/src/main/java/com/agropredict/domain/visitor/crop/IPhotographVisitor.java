package com.agropredict.domain.visitor.crop;

public interface IPhotographVisitor {
    void visitImage(String identifier, String filePath);
}