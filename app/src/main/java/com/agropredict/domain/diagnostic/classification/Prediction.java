package com.agropredict.domain.diagnostic.classification;

import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.guard.ConfidencePrecondition;

public final class Prediction {
    private static final double CONFIDENCE_THRESHOLD = 0.45;

    private final String predictedCrop;
    private final double confidence;

    public Prediction(String predictedCrop, double confidence) {
        this.predictedCrop = ArgumentPrecondition.validate(predictedCrop, "prediction predicted crop");
        this.confidence = ConfidencePrecondition.validate(confidence);
    }

    public void classify(IPredictionConsumer consumer) {
        consumer.classify(predictedCrop, confidence);
    }

    public IImageClassifierResult resolve(String unconfidentMessage) {
        if (isConfident()) {
            return new ConfidentClassification(predictedCrop, confidence);
        }
        return new UnconfidentClassification(unconfidentMessage);
    }

    public boolean isConfident() {
        return confidence >= CONFIDENCE_THRESHOLD;
    }
}
