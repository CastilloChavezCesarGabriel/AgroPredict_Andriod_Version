package com.agropredict.application.usecase;

import com.agropredict.application.repository.IRecordEraser;
import com.agropredict.application.operation_result.IUseCaseResult;
import com.agropredict.application.operation_result.SuccessfulOperation;
import com.agropredict.application.operation_result.FailedOperation;
import java.util.Objects;

public final class DeleteUseCase {
    private final IRecordEraser repository;

    public DeleteUseCase(IRecordEraser repository) {
        this.repository = Objects.requireNonNull(repository, "delete use case requires a record eraser");
    }

    public IUseCaseResult delete(String identifier) {
        try {
            repository.erase(identifier);
            return new SuccessfulOperation(identifier);
        } catch (RuntimeException exception) {
            return new FailedOperation();
        }
    }
}