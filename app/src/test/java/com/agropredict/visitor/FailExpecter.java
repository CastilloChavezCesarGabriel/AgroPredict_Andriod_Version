package com.agropredict.visitor;

import com.agropredict.application.visitor.IOperationResult;

public final class FailExpecter implements IOperationResult {
    @Override
    public void onSucceed(String value) {
        throw new AssertionError("expected onFail but got onSucceed: " + value);
    }

    @Override
    public void onFail() {}

    @Override
    public void onReject(String reason) {
        throw new AssertionError("expected onFail but got onReject: " + reason);
    }
}
