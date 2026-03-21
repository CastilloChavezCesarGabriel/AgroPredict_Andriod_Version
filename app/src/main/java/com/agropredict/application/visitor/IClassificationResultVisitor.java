package com.agropredict.application.visitor;

public interface IClassificationResultVisitor {
    void visit(String predictedCrop, double confidence);
    void reject(String errorMessage);
}