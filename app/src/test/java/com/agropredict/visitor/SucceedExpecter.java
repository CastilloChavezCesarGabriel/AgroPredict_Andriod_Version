package com.agropredict.visitor;

import com.agropredict.application.operation_result.IOperationResult;

public final class SucceedExpecter implements IOperationResult {
    private final String expectedIdentifier;

    public SucceedExpecter(String expectedIdentifier) {
        this.expectedIdentifier = expectedIdentifier;
    }

    @Override
    public void onSucceed(String value) {
        if (expectedIdentifier != null && !expectedIdentifier.equals(value)) {
            throw new AssertionError("expected identifier '" + expectedIdentifier + "' but got '" + value + "'");
        }
    }

    @Override
    public void onFail() {
        throw new AssertionError("expected onSucceed but got onFail");
    }

    @Override
    public void onReject(String reason) {
        throw new AssertionError("expected onSucceed but got onReject: " + reason);
    }
}
