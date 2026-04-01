package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import com.agropredict.infrastructure.persistence.database.IRow;

public final class DiagnosticPersistenceVisitor implements IDiagnosticVisitor {
    private final IRow row;

    public DiagnosticPersistenceVisitor(IRow row) {
        this.row = row;
    }

    @Override
    public void visitIdentity(String identifier) {
        row.record("id", identifier);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        row.record("predicted_crop", predictedCrop);
        row.record("confidence", String.valueOf(confidence));
    }

    @Override
    public void visitAssessment(String severity, String shortSummary) {
        row.record("severity", severity);
        row.record("short_summary", shortSummary);
    }

    @Override
    public void visitRecommendation(String recommendationText) {
        row.record("recommendation_text", recommendationText);
    }
}
