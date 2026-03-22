package com.agropredict.infrastructure.persistence.visitor;

import com.agropredict.domain.component.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.component.diagnostic.DiagnosticConditions;
import com.agropredict.domain.component.diagnostic.DiagnosticContent;
import com.agropredict.domain.component.diagnostic.DiagnosticContext;
import com.agropredict.domain.component.diagnostic.DiagnosticData;
import com.agropredict.domain.component.diagnostic.DiagnosticEnvironment;
import com.agropredict.domain.component.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.component.diagnostic.DiagnosticSummary;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticAssessmentVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticConditionsVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticContentVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticContextVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticDataVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticEnvironmentVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticOwnershipVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticSummaryVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import com.agropredict.domain.visitor.diagnostic.IPredictionVisitor;
import com.agropredict.infrastructure.persistence.IRow;

public final class DiagnosticPersistenceVisitor implements IDiagnosticVisitor,
        IDiagnosticDataVisitor, IDiagnosticContentVisitor,
        IDiagnosticConditionsVisitor, IDiagnosticAssessmentVisitor,
        IPredictionVisitor, IDiagnosticContextVisitor, IDiagnosticEnvironmentVisitor,
        IDiagnosticSummaryVisitor, IDiagnosticOwnershipVisitor {

    private final IRow row;

    public DiagnosticPersistenceVisitor(IRow row) {
        this.row = row;
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
        row.record("id", identifier);
        data.accept(this);
    }

    @Override
    public void visit(Prediction prediction, DiagnosticContent content) {
        if (prediction != null) prediction.accept(this);
        if (content != null) content.accept(this);
    }

    @Override
    public void visit(DiagnosticConditions conditions, DiagnosticAssessment assessment) {
        if (conditions != null) conditions.accept(this);
        if (assessment != null) assessment.accept(this);
    }

    @Override
    public void visit(DiagnosticContext context, DiagnosticEnvironment environment) {
        if (context != null) context.accept(this);
        if (environment != null) environment.accept(this);
    }

    @Override
    public void visit(DiagnosticSummary summary, DiagnosticOwnership ownership) {
        if (summary != null) summary.accept(this);
        if (ownership != null) ownership.accept(this);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        row.record("predicted_crop", predictedCrop);
        row.record("confidence", String.valueOf(confidence));
    }

    @Override
    public void visitContext(String cropIdentifier, String imageIdentifier) {
        row.record("crop_id", cropIdentifier);
        row.record("image_id", imageIdentifier);
    }

    @Override
    public void visitEnvironment(double temperature, double humidity) {
        row.record("temperature", String.valueOf(temperature));
        row.record("humidity", String.valueOf(humidity));
    }

    @Override
    public void visitSummary(String severity, String shortSummary) {
        row.record("severity", severity);
        row.record("short_summary", shortSummary);
    }

    @Override
    public void visitOwnership(String userIdentifier, String recommendationText) {
        row.record("user_id", userIdentifier);
        row.record("recommendation_text", recommendationText);
    }
}
