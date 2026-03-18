package com.agropredict.domain.visitor;

public interface ICropLocationVisitor {
    void visitLocation(String location, String plantingDate);
}