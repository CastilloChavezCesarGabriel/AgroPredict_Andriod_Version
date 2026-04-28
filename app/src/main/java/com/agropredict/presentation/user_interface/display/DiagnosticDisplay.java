package com.agropredict.presentation.user_interface.display;

import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public abstract class DiagnosticDisplay implements IDiagnosticVisitor {
    protected final PredictionDisplay predictionDisplay;

    protected DiagnosticDisplay(PredictionDisplay predictionDisplay) {
        this.predictionDisplay = predictionDisplay;
    }

    public void display(Diagnostic diagnostic) {
        diagnostic.accept(this);
    }

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        predictionDisplay.classify(predictedCrop, confidence);
    }
}