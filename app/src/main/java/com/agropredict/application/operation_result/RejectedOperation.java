package com.agropredict.application.operation_result;

import com.agropredict.domain.guard.ArgumentPrecondition;

public final class RejectedOperation implements IUseCaseResult {
    private final String reason;

    public RejectedOperation(String reason) {
        this.reason = ArgumentPrecondition.validate(reason, "rejected operation reason");
    }

    @Override
    public void accept(IOperationResult result) {
        result.onReject(reason);
    }
}