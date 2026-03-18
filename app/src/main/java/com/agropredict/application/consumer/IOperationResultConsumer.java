package com.agropredict.application.consumer;

public interface IOperationResultConsumer {
    void visit(boolean completed, String resultIdentifier);
}