package com.agropredict.domain.visitor.diagnostic;

public interface IPredictionVisitor {
    void visitPrediction(String predictedCrop, double confidence);
}