package com.agropredict.domain.visitor.crop;

public interface ICropSoilVisitor {
    void visitSoil(String soilTypeIdentifier, String area);
}