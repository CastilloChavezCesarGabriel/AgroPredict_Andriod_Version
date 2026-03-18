package com.agropredict.application.result;

import com.agropredict.application.consumer.IOperationResultConsumer;

public final class OperationResult {
    private final boolean completed;
    private final String resultIdentifier;

    private OperationResult(boolean completed, String resultIdentifier) {
        this.completed = completed;
        this.resultIdentifier = resultIdentifier;
    }

    public static OperationResult succeed(String resultIdentifier) {
        return new OperationResult(true, resultIdentifier);
    }

    public static OperationResult fail() {
        return new OperationResult(false, null);
    }

    public void accept(IOperationResultConsumer visitor) {
        visitor.visit(completed, resultIdentifier);
    }
}