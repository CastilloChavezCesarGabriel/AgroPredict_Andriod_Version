package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.request.CropUpdateRequest;
import java.util.Objects;

public final class UpdateCropUseCase {
    private final ICropRepository cropRepository;

    public UpdateCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = Objects.requireNonNull(cropRepository, "update crop use case requires a crop repository");
    }

    public void update(CropUpdateRequest request) {
        cropRepository.update(request);
    }
}