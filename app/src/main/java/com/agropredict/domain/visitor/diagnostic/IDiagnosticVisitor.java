package com.agropredict.domain.visitor.diagnostic;

public interface IDiagnosticVisitor {
    default void visitIdentity(String identifier) {}
    default void visitPrediction(String predictedCrop, double confidence) {}
    default void visitAssessment(String severity, String shortSummary) {}
    default void visitRecommendation(String recommendationText) {}
}