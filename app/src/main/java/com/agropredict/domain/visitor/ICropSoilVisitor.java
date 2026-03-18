package com.agropredict.domain.visitor;

public interface ICropSoilVisitor {
    void visitSoil(String soilTypeIdentifier, String area);
}