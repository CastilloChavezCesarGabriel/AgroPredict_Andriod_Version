package com.agropredict.domain.diagnostic.classification;

import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.guard.ConfidencePrecondition;

public final class ConfidentClassification implements IImageClassifierResult {
    private final String predictedCrop;
    private final double confidence;

    public ConfidentClassification(String predictedCrop, double confidence) {
        this.predictedCrop = ArgumentPrecondition.validate(predictedCrop, "confident classification predicted crop");
        this.confidence = ConfidencePrecondition.validate(confidence);
    }

    @Override
    public void accept(IClassificationResult visitor) {
        visitor.onClassify(predictedCrop, confidence);
    }
}
