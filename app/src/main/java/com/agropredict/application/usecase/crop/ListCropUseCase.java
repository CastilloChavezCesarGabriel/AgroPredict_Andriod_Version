package com.agropredict.application.usecase.crop;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.domain.entity.Crop;
import java.util.List;

public final class ListCropUseCase {
    private final ICropRepository cropRepository;

    public ListCropUseCase(ICropRepository cropRepository) {
        this.cropRepository = cropRepository;
    }

    public List<Crop> list(String userIdentifier) {
        return cropRepository.list(userIdentifier);
    }
}