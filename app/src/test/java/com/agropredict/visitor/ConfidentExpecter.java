package com.agropredict.visitor;

import com.agropredict.domain.diagnostic.visitor.IClassificationResult;

public final class ConfidentExpecter implements IClassificationResult {
    private final String expectedCrop;
    private final double minimumConfidence;

    public ConfidentExpecter(String expectedCrop, double minimumConfidence) {
        this.expectedCrop = expectedCrop;
        this.minimumConfidence = minimumConfidence;
    }

    @Override
    public void onClassify(String predictedCrop, double confidence) {
        if (expectedCrop != null && !expectedCrop.equals(predictedCrop)) {
            throw new AssertionError("expected crop '" + expectedCrop + "' but got '" + predictedCrop + "'");
        }
        if (confidence < minimumConfidence) {
            throw new AssertionError("expected confidence >= " + minimumConfidence + " but got " + confidence);
        }
    }

    @Override
    public void onReject(String reason) {
        throw new AssertionError("expected onClassify but got onReject: " + reason);
    }
}
