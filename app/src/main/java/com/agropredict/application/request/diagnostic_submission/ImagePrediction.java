package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.Prediction;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.guard.ConfidencePrecondition;

public final class ImagePrediction {
    private final String predictedCrop;
    private final double confidence;

    public ImagePrediction(String predictedCrop, double confidence) {
        this.predictedCrop = ArgumentPrecondition.validate(predictedCrop, "image prediction predicted crop");
        this.confidence = ConfidencePrecondition.validate(confidence);
    }

    public Diagnostic diagnose(String identifier) {
        return new Diagnostic(identifier, new Prediction(predictedCrop, confidence));
    }

    public Cultivation produce(String stage) {
        return new Cultivation(predictedCrop, stage);
    }

    public void accept(IPredictionConsumer consumer) {
        consumer.classify(predictedCrop, confidence);
    }
}