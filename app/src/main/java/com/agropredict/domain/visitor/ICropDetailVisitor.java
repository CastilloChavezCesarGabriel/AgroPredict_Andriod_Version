package com.agropredict.domain.visitor;

public interface ICropDetailVisitor {
    void visit(String cropType, String fieldName);
}