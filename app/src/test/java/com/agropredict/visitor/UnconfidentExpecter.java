package com.agropredict.visitor;

import com.agropredict.domain.diagnostic.visitor.IClassificationResult;

public final class UnconfidentExpecter implements IClassificationResult {
    private final String expectedReason;

    public UnconfidentExpecter(String expectedReason) {
        this.expectedReason = expectedReason;
    }

    @Override
    public void onClassify(String predictedCrop, double confidence) {
        throw new AssertionError("expected onReject but got onClassify: " + predictedCrop + " (" + confidence + ")");
    }

    @Override
    public void onReject(String reason) {
        if (expectedReason != null && !expectedReason.equals(reason)) {
            throw new AssertionError("expected reason '" + expectedReason + "' but got '" + reason + "'");
        }
    }
}
