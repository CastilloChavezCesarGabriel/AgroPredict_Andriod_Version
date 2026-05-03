package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.Session;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import com.agropredict.domain.visitor.session.ISessionVisitor;
import com.agropredict.infrastructure.persistence.database.IRow;

public final class DiagnosticPersistenceVisitor implements IDiagnosticVisitor, ISessionVisitor {
    private final IRow row;

    public DiagnosticPersistenceVisitor(IRow row, Session session) {
        this.row = row;
        if (session != null) session.accept(this);
    }

    @Override
    public void visit(String userIdentifier, String occupation) {
        row.record("user_id", userIdentifier);
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
    public void visitSeverity(String value) {
        row.record("severity", value);
    }

    @Override
    public void visitSummary(String text) {
        row.record("short_summary", text);
    }

    @Override
    public void visitRecommendation(String text) {
        row.record("recommendation_text", text);
    }

    @Override
    public void visitCrop(String identifier) {
        row.record("crop_id", identifier);
    }

    @Override
    public void visitImage(String identifier) {
        row.record("image_id", identifier);
    }
}
