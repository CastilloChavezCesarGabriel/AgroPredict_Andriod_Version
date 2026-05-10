package com.agropredict.application.operation_result;

import com.agropredict.application.visitor.IOperationResult;
import com.agropredict.domain.guard.ArgumentPrecondition;

public final class SuccessfulOperation implements IUseCaseResult {
    private final String value;

    public SuccessfulOperation(String value) {
        this.value = ArgumentPrecondition.validate(value, "successful operation value");
    }

    @Override
    public void accept(IOperationResult result) {
        result.onSucceed(value);
    }
}