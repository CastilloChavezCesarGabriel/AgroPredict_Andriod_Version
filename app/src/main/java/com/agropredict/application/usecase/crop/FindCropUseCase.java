package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.entity.Crop;

public final class FindCropUseCase {
    private final ICropRepository cropRepository;

    public FindCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public Crop find(String cropIdentifier) {
        return cropRepository.find(cropIdentifier);
    }
}