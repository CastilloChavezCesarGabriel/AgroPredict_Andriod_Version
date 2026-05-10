package com.agropredict.application.operation_result;

import com.agropredict.application.visitor.IOperationResult;

public final class FailedOperation implements IUseCaseResult {
    @Override
    public void accept(IOperationResult result) {
        result.onFail();
    }
}