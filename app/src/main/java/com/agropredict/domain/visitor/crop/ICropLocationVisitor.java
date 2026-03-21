package com.agropredict.domain.visitor.crop;

public interface ICropLocationVisitor {
    void visitLocation(String location, String plantingDate);
}