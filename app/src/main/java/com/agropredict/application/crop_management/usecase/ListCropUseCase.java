package com.agropredict.application.crop_management.usecase;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.crop.Crop;
import java.util.List;
import java.util.Objects;

public final class ListCropUseCase {
    private final ICropRepository cropRepository;

    public ListCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = Objects.requireNonNull(cropRepository, "list crop use case requires a crop repository");
    }

    public List<Crop> list(String userIdentifier) {
        return cropRepository.list(userIdentifier);
    }
}
