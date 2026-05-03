package com.agropredict.application.usecase.authentication;

import com.agropredict.application.operation_result.IOperationResultSource;
import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.domain.ILoginGate;

public final class LoginGate implements ILoginGate {
    private OperationResult outcome;

    @Override
    public void allow() {}

    @Override
    public void block() {
        outcome = OperationResult.reject("Account locked. Try again in a few minutes.");
    }

    @Override
    public void exhaust() {
        outcome = OperationResult.reject("Too many attempts. Account locked for 5 minutes.");
    }

    public OperationResult resolve(IOperationResultSource source) {
        return outcome != null ? outcome : source.compute();
    }
}