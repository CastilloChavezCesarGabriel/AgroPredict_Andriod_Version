package com.agropredict.application.diagnostic_submission.workflow;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.photograph.Photograph;
import java.util.Objects;

public final class CropRegistry {
    private final CropDossier dossier;
    private final ICatalogRepository stages;

    public CropRegistry(CropDossier dossier, ICatalogRepository stages) {
        this.dossier = Objects.requireNonNull(dossier, "crop registry requires a dossier");
        this.stages = Objects.requireNonNull(stages, "crop registry requires a stages catalog");
    }

    public void register(Crop crop, Photograph photograph) {
        dossier.archive(crop, photograph);
    }

    public String resolve(String stageName) {
        return stages.resolve(stageName);
    }
}