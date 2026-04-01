package com.agropredict.application.operation_result;

import com.agropredict.application.visitor.IClassificationResultVisitor;
import com.agropredict.domain.component.diagnostic.Prediction;

public final class ClassificationResult {
    private final String predictedCrop;
    private final double confidence;
    private final Prediction prediction;

    public ClassificationResult(String predictedCrop, double confidence) {
        this.predictedCrop = predictedCrop;
        this.confidence = confidence;
        this.prediction = new Prediction(predictedCrop, confidence);
    }

    public void accept(IClassificationResultVisitor visitor) {
        if (prediction.isConfident()) {
            visitor.visitPrediction(predictedCrop, confidence);
        } else {
            visitor.reject("Could not identify the crop with certainty");
        }
    }
}