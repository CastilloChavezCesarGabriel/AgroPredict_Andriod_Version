package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.entity.Crop;

public final class UpdateCropUseCase {
    private final ICropRepository cropRepository;

    public UpdateCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public void update(Crop crop) {
        cropRepository.update(crop);
    }
}