package com.agropredict.application.result;

import com.agropredict.application.visitor.IClassificationResultVisitor;

public final class ClassificationResult {
    private final String predictedCrop;
    private final double confidence;

    public ClassificationResult(String predictedCrop, double confidence) {
        this.predictedCrop = predictedCrop;
        this.confidence = confidence;
    }

    public void accept(IClassificationResultVisitor consumer) {
        consumer.visit(predictedCrop, confidence);
    }
}