package com.agropredict.domain.visitor.crop;

public interface ICropImageVisitor {
    void visit(String identifier, String filePath);
}