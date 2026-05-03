package com.agropredict.domain.visitor.diagnostic;

public interface IDiagnosticVisitor {
    default void visitIdentity(String identifier) {}
    default void visitPrediction(String predictedCrop, double confidence) {}
    default void visitSeverity(String value) {}
    default void visitSummary(String text) {}
    default void visitRecommendation(String text) {}
    default void visitCrop(String identifier) {}
    default void visitImage(String identifier) {}
}
