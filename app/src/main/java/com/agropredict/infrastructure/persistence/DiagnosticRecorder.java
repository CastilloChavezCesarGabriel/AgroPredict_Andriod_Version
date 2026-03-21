package com.agropredict.infrastructure.persistence;

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

public final class DiagnosticRecorder implements IDiagnosticVisitor,
        IDiagnosticDataVisitor, IDiagnosticContentVisitor,
        IDiagnosticConditionsVisitor, IDiagnosticAssessmentVisitor,
        IPredictionVisitor, IDiagnosticContextVisitor, IDiagnosticEnvironmentVisitor,
        IDiagnosticSummaryVisitor, IDiagnosticOwnershipVisitor {

    private final IRecord record;

    public DiagnosticRecorder(IRecord record) {
        this.record = record;
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
        record.record("id", identifier);
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
        record.record("predicted_crop", predictedCrop);
        record.record("confidence", String.valueOf(confidence));
    }

    @Override
    public void visitContext(String cropIdentifier, String imageIdentifier) {
        record.record("crop_id", cropIdentifier);
        record.record("image_id", imageIdentifier);
    }

    @Override
    public void visitEnvironment(double temperature, double humidity) {
        record.record("temperature", String.valueOf(temperature));
        record.record("humidity", String.valueOf(humidity));
    }

    @Override
    public void visitSummary(String severity, String shortSummary) {
        record.record("severity", severity);
        record.record("short_summary", shortSummary);
    }

    @Override
    public void visitOwnership(String userIdentifier, String recommendationText) {
        record.record("user_id", userIdentifier);
        record.record("recommendation_text", recommendationText);
    }
}
