package com.agropredict.application.usecase.crop;

import com.agropredict.application.operation_result.OperationResult;
import com.agropredict.application.repository.IClearable;
import com.agropredict.application.repository.ICropRepository;

public final class RemoveCropUseCase {
    private final ICropRepository cropRepository;
    private final IClearable cleanup;

    public RemoveCropUseCase(ICropRepository cropRepository, IClearable cleanup) {
        this.cropRepository = cropRepository;
        this.cleanup = cleanup;
    }

    public OperationResult remove(String cropIdentifier) {
        try {
            cleanup.clear(cropIdentifier);
            cropRepository.delete(cropIdentifier);
            return OperationResult.succeed(cropIdentifier);
        } catch (RuntimeException exception) {
            return OperationResult.fail();
        }
    }
}
