package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.entity.Crop;

public final class LoadCropDetailUseCase {
    private final ICropRepository cropRepository;

    public LoadCropDetailUseCase(ICropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public Crop load(String cropIdentifier) {
        return cropRepository.load(cropIdentifier);
    }
}