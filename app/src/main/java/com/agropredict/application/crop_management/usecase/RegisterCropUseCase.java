package com.agropredict.application.crop_management.usecase;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.crop.Crop;
import java.util.Objects;

public final class RegisterCropUseCase {
    private final ICropRepository cropRepository;

    public RegisterCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = Objects.requireNonNull(cropRepository, "register crop use case requires a crop repository");
    }

    public void register(Crop crop) {
        cropRepository.store(crop);
    }
}
