package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.domain.Identifier;
import com.agropredict.domain.entity.Crop;

public final class Cultivation {
    private final String predictedCrop;
    private final String stage;

    public Cultivation(String predictedCrop, String stage) {
        this.predictedCrop = predictedCrop;
        this.stage = stage;
    }

    public Crop cultivate() {
        Crop crop = new Crop(Identifier.generate("crop"), predictedCrop);
        if (stage != null) crop.schedule(null, stage);
        return crop;
    }
}