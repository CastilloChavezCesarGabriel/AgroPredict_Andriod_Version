package com.agropredict.application.result;

import com.agropredict.application.visitor.IClassificationResultVisitor;
import com.agropredict.domain.component.diagnostic.Prediction;

public final class ClassificationResult {
    private final Prediction prediction;

    public ClassificationResult(String predictedCrop, double confidence) {
        this.prediction = new Prediction(predictedCrop, confidence);
    }

    public void accept(IClassificationResultVisitor visitor) {
        if (prediction.isConfident()) {
            prediction.accept(visitor);
        } else {
            visitor.reject("Could not identify the crop with certainty");
        }
    }
}