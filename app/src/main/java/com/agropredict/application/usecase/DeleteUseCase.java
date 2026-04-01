package com.agropredict.application.usecase;

import com.agropredict.application.repository.IDeletable;
import com.agropredict.application.operation_result.OperationResult;

public final class DeleteUseCase {
    private final IDeletable repository;

    public DeleteUseCase(IDeletable repository) {
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