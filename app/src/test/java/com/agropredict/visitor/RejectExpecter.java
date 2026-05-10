package com.agropredict.visitor;

import com.agropredict.application.visitor.IOperationResult;

public final class RejectExpecter implements IOperationResult {
    private final String expectedReason;

    public RejectExpecter(String expectedReason) {
        this.expectedReason = expectedReason;
    }

    @Override
    public void onSucceed(String value) {
        throw new AssertionError("expected onReject but got onSucceed: " + value);
    }

    @Override
    public void onFail() {
        throw new AssertionError("expected onReject but got onFail");
    }

    @Override
    public void onReject(String reason) {
        if (expectedReason != null && !expectedReason.equals(reason)) {
            throw new AssertionError("expected reason '" + expectedReason + "' but got '" + reason + "'");
        }
    }
}
