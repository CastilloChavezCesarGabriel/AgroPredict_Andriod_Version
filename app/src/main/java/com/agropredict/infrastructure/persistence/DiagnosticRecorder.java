package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import com.agropredict.domain.value.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.value.diagnostic.DiagnosticConditions;
import com.agropredict.domain.value.diagnostic.DiagnosticContent;
import com.agropredict.domain.value.diagnostic.DiagnosticContext;
import com.agropredict.domain.value.diagnostic.DiagnosticData;
import com.agropredict.domain.value.diagnostic.DiagnosticEnvironment;
import com.agropredict.domain.value.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.value.diagnostic.DiagnosticSummary;
import com.agropredict.domain.value.diagnostic.Prediction;
import com.agropredict.domain.visitor.IDiagnosticAssessmentVisitor;
import com.agropredict.domain.visitor.IDiagnosticConditionsVisitor;
import com.agropredict.domain.visitor.IDiagnosticContentVisitor;
import com.agropredict.domain.visitor.IDiagnosticContextVisitor;
import com.agropredict.domain.visitor.IDiagnosticDataVisitor;
import com.agropredict.domain.visitor.IDiagnosticEnvironmentVisitor;
import com.agropredict.domain.visitor.IDiagnosticOwnershipVisitor;
import com.agropredict.domain.visitor.IDiagnosticSummaryVisitor;
import com.agropredict.domain.visitor.IDiagnosticVisitor;
import com.agropredict.domain.visitor.IPredictionVisitor;

public final class DiagnosticRecorder implements IDiagnosticVisitor,
        IDiagnosticDataVisitor, IDiagnosticContentVisitor,
        IDiagnosticConditionsVisitor, IDiagnosticAssessmentVisitor,
        IPredictionVisitor, IDiagnosticContextVisitor, IDiagnosticEnvironmentVisitor,
        IDiagnosticSummaryVisitor, IDiagnosticOwnershipVisitor {

    private final ContentValues values;

    public DiagnosticRecorder(ContentValues values) {
        this.values = values;
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
        values.put("id", identifier);
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
        values.put("predicted_crop", predictedCrop);
        values.put("confidence", confidence);
    }

    @Override
    public void visitContext(String cropIdentifier, String imageIdentifier) {
        values.put("crop_id", cropIdentifier);
        values.put("image_id", imageIdentifier);
    }

    @Override
    public void visitEnvironment(double temperature, double humidity) {
        values.put("temperature", temperature);
        values.put("humidity", humidity);
    }

    @Override
    public void visitSummary(String severity, String shortSummary) {
        values.put("severity", severity);
        values.put("short_summary", shortSummary);
    }

    @Override
    public void visitOwnership(String userIdentifier, String recommendationText) {
        values.put("user_id", userIdentifier);
        values.put("recommendation_text", recommendationText);
    }
}
