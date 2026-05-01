package com.agropredict.application.usecase;

import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.application.operation_result.OperationResult;

public final class DeleteUseCase {
    private final IRecordEraser repository;

    public DeleteUseCase(IRecordEraser repository) {
        this.repository = repository;
    }

    public OperationResult delete(String identifier) {
        try {
            repository.delete(identifier);
            return OperationResult.succeed(identifier);
        } catch (RuntimeException exception) {
            return OperationResult.fail();
        }
    }
}