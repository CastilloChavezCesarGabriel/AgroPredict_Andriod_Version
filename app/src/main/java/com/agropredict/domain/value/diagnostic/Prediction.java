package com.agropredict.domain.value.diagnostic;

import com.agropredict.domain.visitor.IPredictionVisitor;

public final class Prediction {
    private static final double CONFIDENCE_THRESHOLD = 0.6;

    private final String predictedCrop;
    private final double confidence;

    public Prediction(String predictedCrop, double confidence) {
        this.predictedCrop = predictedCrop;
        this.confidence = confidence;
    }

    public void accept(IPredictionVisitor visitor) {
        visitor.visitPrediction(predictedCrop, confidence);
    }

    public boolean isConfident() {
        return confidence >= CONFIDENCE_THRESHOLD;
    }
}