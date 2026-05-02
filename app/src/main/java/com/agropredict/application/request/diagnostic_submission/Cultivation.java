package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.domain.Identifier;
import com.agropredict.domain.component.crop.CropProfile;
import com.agropredict.domain.component.crop.GrowthCycle;
import com.agropredict.domain.entity.Crop;

public final class Cultivation {
    private final String predictedCrop;
    private final String stageName;

    public Cultivation(String predictedCrop, String stageName) {
        this.predictedCrop = predictedCrop;
        this.stageName = stageName;
    }

    public Crop cultivate(ICatalogRepository stageCatalog) {
        String stageIdentifier = stageName != null ? stageCatalog.resolve(stageName) : null;
        GrowthCycle growth = stageIdentifier != null ? new GrowthCycle(null, stageIdentifier) : null;
        CropProfile profile = new CropProfile(null, null, growth);
        return new Crop(Identifier.generate("crop"), predictedCrop, profile);
    }
}