package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class Prediction {
    private static final double CONFIDENCE_THRESHOLD = 0.45;

    private final String predictedCrop;
    private final double confidence;

    public Prediction(String predictedCrop, double confidence) {
        this.predictedCrop = predictedCrop;
        this.confidence = confidence;
    }

    public void accept(IDiagnosticVisitor visitor) {
        visitor.visitPrediction(predictedCrop, confidence);
    }

    public boolean isConfident() {
        return confidence >= CONFIDENCE_THRESHOLD;
    }
}