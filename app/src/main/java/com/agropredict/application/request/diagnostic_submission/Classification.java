package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.component.diagnostic.Prediction;

public final class Classification {
    private final String predictedCrop;
    private final double confidence;

    public Classification(String predictedCrop, double confidence) {
        this.predictedCrop = predictedCrop;
        this.confidence = confidence;
    }

    public Diagnostic derive(String identifier) {
        Prediction prediction = new Prediction(predictedCrop, confidence);
        return new Diagnostic(identifier, prediction);
    }

    public Cultivation cultivate(String stage) {
        return new Cultivation(predictedCrop, stage);
    }

    public void accept(ISubmissionVisitor visitor) {
        visitor.visitPrediction(predictedCrop, confidence);
    }
}
