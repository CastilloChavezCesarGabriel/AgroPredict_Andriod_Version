package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.entity.Crop;

public final class SubmissionField {
    private final Cultivation crop;
    private final PhotographInput image;

    public SubmissionField(Cultivation crop, PhotographInput image) {
        this.crop = crop;
        this.image = image;
    }

    public void store(ICropRepository cropRepository, IPhotographRepository photoRepository) {
        Crop entity = crop.cultivate();
        cropRepository.store(entity);
        image.store(photoRepository, entity);
    }
}