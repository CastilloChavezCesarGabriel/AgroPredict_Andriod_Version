package com.agropredict.application.operation_result;

import com.agropredict.application.visitor.IOperationResult;

public interface IUseCaseResult {
    void accept(IOperationResult result);
}