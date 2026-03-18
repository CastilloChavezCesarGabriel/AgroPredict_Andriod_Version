package com.agropredict.domain.visitor;

public interface IPredictionVisitor {
    void visitPrediction(String predictedCrop, double confidence);
}