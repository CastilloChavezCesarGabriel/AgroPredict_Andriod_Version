package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.diagnostic_submission.Cropland;
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

    public Crop cultivate(String identifier, Cropland cropland) {
        String stageIdentifier = stageName != null ? cropland.resolve(stageName) : null;
        GrowthCycle growth = stageIdentifier != null ? new GrowthCycle(null, stageIdentifier) : null;
        CropProfile profile = new CropProfile(null, null, growth);
        return new Crop(identifier, predictedCrop, profile);
    }
}
