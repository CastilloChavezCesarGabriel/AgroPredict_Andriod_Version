package com.agropredict.domain.visitor;

public interface ICropImageVisitor {
    void visit(String identifier, String filePath);
}