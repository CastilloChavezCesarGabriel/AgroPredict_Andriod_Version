package com.agropredict.application.crop_management.usecase;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.crop.Crop;
import java.util.Objects;

public final class FindCropUseCase {
    private final ICropRepository cropRepository;

    public FindCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = Objects.requireNonNull(cropRepository, "find crop use case requires a crop repository");
    }

    public Crop find(String cropIdentifier) {
        return cropRepository.find(cropIdentifier);
    }
}
