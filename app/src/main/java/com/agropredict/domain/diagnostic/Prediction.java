package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;

public final class Prediction {
    private static final double CONFIDENCE_THRESHOLD = 0.45;

    private final String predictedCrop;
    private final double confidence;

    public Prediction(String predictedCrop, double confidence) {
        this.predictedCrop = predictedCrop;
        this.confidence = confidence;
    }

    public void classify(IPredictionConsumer consumer) {
        consumer.classify(predictedCrop, confidence);
    }

    public boolean isConfident() {
        return confidence >= CONFIDENCE_THRESHOLD;
    }
}