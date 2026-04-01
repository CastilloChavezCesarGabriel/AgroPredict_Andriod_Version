package com.agropredict.domain.visitor.crop;

public interface ICropVisitor {
    void visitIdentity(String identifier, String cropType);
    void visitField(String name, String location);
    void visitSoil(String typeIdentifier, String area);
    void visitPlanting(String date, String stageIdentifier);
}