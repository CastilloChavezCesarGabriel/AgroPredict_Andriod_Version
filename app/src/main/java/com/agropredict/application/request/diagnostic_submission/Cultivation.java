package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.domain.Identifier;
import com.agropredict.domain.component.crop.CropProfile;
import com.agropredict.domain.component.crop.GrowthCycle;
import com.agropredict.domain.entity.Crop;

public final class Cultivation {
    private final String predictedCrop;
    private final String stage;

    public Cultivation(String predictedCrop, String stage) {
        this.predictedCrop = predictedCrop;
        this.stage = stage;
    }

    public Crop cultivate() {
        GrowthCycle growth = stage != null ? new GrowthCycle(null, stage) : null;
        CropProfile profile = new CropProfile(null, null, growth);
        return new Crop(Identifier.generate("crop"), predictedCrop, profile);
    }
}