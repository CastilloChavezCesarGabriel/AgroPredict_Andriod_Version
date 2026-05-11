package com.agropredict.application.diagnostic_submission.workflow;

import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.photograph.Photograph;
import java.util.Objects;

public final class CropDossier {
    private final ICropRepository cropRepository;
    private final IPhotographRepository photographRepository;

    public CropDossier(ICropRepository cropRepository, IPhotographRepository photographRepository) {
        this.cropRepository = Objects.requireNonNull(cropRepository, "crop dossier requires a crop repository");
        this.photographRepository = Objects.requireNonNull(photographRepository, "crop dossier requires a photograph repository");
    }

    public void archive(Crop crop, Photograph photograph) {
        cropRepository.store(crop);
        photographRepository.store(photograph, crop);
    }
}