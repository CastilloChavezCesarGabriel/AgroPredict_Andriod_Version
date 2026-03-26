package com.agropredict.application.request.data;

import com.agropredict.application.visitor.ISubmissionVisitor;
import com.agropredict.application.request.input.Cultivation;
import com.agropredict.domain.Identifier;
import com.agropredict.domain.entity.Diagnostic;
import com.agropredict.domain.component.diagnostic.DiagnosticData;
import com.agropredict.domain.component.diagnostic.Prediction;

public final class Classification {
    private final String predictedCrop;
    private final double confidence;

    public Classification(String predictedCrop, double confidence) {
        this.predictedCrop = predictedCrop;
        this.confidence = confidence;
    }

    public Diagnostic derive() {
        String identifier = Identifier.generate("diag");
        Prediction prediction = new Prediction(predictedCrop, confidence);
        DiagnosticData data = new DiagnosticData(prediction, null);
        return Diagnostic.create(identifier, data);
    }

    public Cultivation cultivate(String stage) {
        return new Cultivation(predictedCrop, stage);
    }

    public void accept(ISubmissionVisitor visitor) {
        visitor.visitPrediction(predictedCrop, confidence);
    }
}