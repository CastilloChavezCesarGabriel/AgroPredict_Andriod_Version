package com.agropredict.application.diagnostic_submission.request;

import com.agropredict.domain.diagnostic.Diagnostic;
import com.agropredict.domain.diagnostic.classification.Prediction;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.visitor.IPredictionConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.guard.ConfidencePrecondition;
import java.util.Objects;

public final class ImagePrediction {
    private final String predictedCrop;
    private final double confidence;
    private final ISeverity pendingSeverity;

    public ImagePrediction(String predictedCrop, double confidence, ISeverity pendingSeverity) {
        this.predictedCrop = ArgumentPrecondition.validate(predictedCrop, "image prediction predicted crop");
        this.confidence = ConfidencePrecondition.validate(confidence);
        this.pendingSeverity = Objects.requireNonNull(pendingSeverity, "image prediction requires a pending severity");
    }

    public Diagnostic diagnose(String identifier) {
        return Diagnostic.begin(identifier, new Prediction(predictedCrop, confidence), pendingSeverity);
    }

    public void accept(IPredictionConsumer consumer) {
        consumer.classify(predictedCrop, confidence);
    }
}
