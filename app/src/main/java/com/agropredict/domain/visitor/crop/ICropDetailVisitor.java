package com.agropredict.domain.visitor.crop;

public interface ICropDetailVisitor {
    void visit(String cropType, String fieldName);
}