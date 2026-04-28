package com.agropredict.domain.visitor.photograph;

public interface IPhotographVisitor {
    default void visitPhotograph(String identifier, String filePath) {}
}