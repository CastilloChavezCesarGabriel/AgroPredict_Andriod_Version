package com.agropredict.visitor;

import com.agropredict.application.visitor.IClassificationResultVisitor;

public final class TestClassificationResultVisitor implements IClassificationResultVisitor {
    private String predictedCrop;
    private double confidence;
    private String errorMessage;

    @Override
    public void visitPrediction(String predictedCrop, double confidence) {
        this.predictedCrop = predictedCrop;
        this.confidence = confidence;
    }

    @Override
    public void onReject(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public boolean wasAccepted() {
        return predictedCrop != null;
    }

    public boolean wasRejected() {
        return errorMessage != null;
    }

    public boolean isAccepted(String crop) {
        return predictedCrop != null && predictedCrop.equals(crop);
    }

    public boolean isRejected(String reason) {
        return errorMessage != null && errorMessage.equals(reason);
    }

    public boolean isConfident(double threshold) {
        return confidence >= threshold;
    }
}