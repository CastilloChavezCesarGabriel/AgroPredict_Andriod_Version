package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.request.CropUpdateRequest;

public final class UpdateCropUseCase {
    private final ICropRepository cropRepository;

    public UpdateCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public void update(CropUpdateRequest request) {
        cropRepository.update(request);
    }
}