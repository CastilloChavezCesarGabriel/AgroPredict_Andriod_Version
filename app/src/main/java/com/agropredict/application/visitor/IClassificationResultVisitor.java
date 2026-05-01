package com.agropredict.application.visitor;

public interface IClassificationResultVisitor {
    void visitPrediction(String predictedCrop, double confidence);
    void onReject(String errorMessage);
}