package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.entity.Crop;

public final class InsertCropUseCase {
    private final ICropRepository cropRepository;

    public InsertCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public void insert(Crop crop) {
        cropRepository.store(crop);
    }
}