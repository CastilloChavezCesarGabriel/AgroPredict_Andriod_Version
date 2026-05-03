package com.agropredict.application.diagnostic_submission;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Photograph;

public final class FieldStorage {
    private final ICropRepository cropRepository;
    private final IPhotographRepository photographRepository;

    public FieldStorage(ICropRepository cropRepository, IPhotographRepository photographRepository) {
        this.cropRepository = cropRepository;
        this.photographRepository = photographRepository;
    }

    public void persist(Crop crop) {
        cropRepository.store(crop);
    }

    public void capture(Photograph photograph, Crop crop) {
        photographRepository.store(photograph, crop);
    }
}
