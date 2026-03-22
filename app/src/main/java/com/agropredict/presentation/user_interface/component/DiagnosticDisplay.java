package com.agropredict.presentation.user_interface.component;

import com.agropredict.domain.component.diagnostic.DiagnosticAssessment;
import com.agropredict.domain.component.diagnostic.DiagnosticConditions;
import com.agropredict.domain.component.diagnostic.DiagnosticContent;
import com.agropredict.domain.component.diagnostic.DiagnosticContext;
import com.agropredict.domain.component.diagnostic.DiagnosticData;
import com.agropredict.domain.component.diagnostic.DiagnosticEnvironment;
import com.agropredict.domain.component.diagnostic.DiagnosticOwnership;
import com.agropredict.domain.component.diagnostic.DiagnosticSummary;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticAssessmentVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticConditionsVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticContentVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticContextVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticDataVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticOwnershipVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticSummaryVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;
import com.agropredict.domain.visitor.diagnostic.IPredictionVisitor;

public abstract class DiagnosticDisplay implements IDiagnosticVisitor,
        IDiagnosticDataVisitor, IDiagnosticContentVisitor,
        IDiagnosticConditionsVisitor, IDiagnosticContextVisitor,
        IDiagnosticAssessmentVisitor, IPredictionVisitor,
        IDiagnosticSummaryVisitor, IDiagnosticOwnershipVisitor {
    protected final PredictionDisplay predictionDisplay;
    protected final PhotoDisplay photoDisplay;

    protected DiagnosticDisplay(PredictionDisplay predictionDisplay, PhotoDisplay photoDisplay) {
        this.predictionDisplay = predictionDisplay;
        this.photoDisplay = photoDisplay;
    }

    public void display(Diagnostic diagnostic) {
        diagnostic.accept(this);
    }

    @Override
    public void visit(String identifier, DiagnosticData data) {
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
    }

    @Override
    public void visitContext(String cropIdentifier, String imageIdentifier) {
        photoDisplay.load(imageIdentifier);
    }

    @Override
    public void visit(DiagnosticSummary summary, DiagnosticOwnership ownership) {
        if (summary != null) summary.accept(this);
        if (ownership != null) ownership.accept(this);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        predictionDisplay.classify(predictedCrop, confidence);
    }
}