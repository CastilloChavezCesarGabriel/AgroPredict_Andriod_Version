package com.agropredict.domain.visitor.crop;

public interface ICropVisitor {
    default void visitIdentity(String identifier, String cropType) {}
    default void visitField(String name, String location) {}
    default void visitSoil(String typeIdentifier, String area) {}
    default void visitPlanting(String date, String stageIdentifier) {}
}