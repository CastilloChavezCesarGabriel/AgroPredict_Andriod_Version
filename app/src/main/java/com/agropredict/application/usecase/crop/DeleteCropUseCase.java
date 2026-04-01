package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.operation_result.OperationResult;

public final class DeleteCropUseCase {
    private final ICropRepository cropRepository;

    public DeleteCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public OperationResult delete(String cropIdentifier) {
        try {
            cropRepository.delete(cropIdentifier);
            return OperationResult.succeed(cropIdentifier);
        } catch (Exception exception) {
            return OperationResult.fail();
        }
    }
}