package com.agropredict.domain.visitor.diagnostic;

public interface IDiagnosticVisitor {
    void visitIdentity(String identifier);
    void visitPrediction(String predictedCrop, double confidence);
    void visitAssessment(String severity, String shortSummary);
    void visitRecommendation(String recommendationText);
}