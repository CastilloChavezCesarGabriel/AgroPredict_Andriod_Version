package com.agropredict.application.operation_result;

public final class FailedOperation implements IUseCaseResult {
    @Override
    public void accept(IOperationResult result) {
        result.onFail();
    }
}