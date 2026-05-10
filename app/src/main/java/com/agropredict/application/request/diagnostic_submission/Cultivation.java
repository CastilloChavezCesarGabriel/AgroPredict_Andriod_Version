package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.diagnostic_submission.CropRegistry;
import com.agropredict.domain.crop.CropProfile;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.guard.ArgumentPrecondition;

public final class Cultivation {
    private final String predictedCrop;
    private final String stageName;

    public Cultivation(String predictedCrop, String stageName) {
        this.predictedCrop = ArgumentPrecondition.validate(predictedCrop, "cultivation predicted crop");
        this.stageName = stageName;
    }

    public Crop produce(String identifier, CropRegistry registry) {
        String stageIdentifier = stageName != null ? registry.resolve(stageName) : null;
        return new Crop(identifier, predictedCrop, CropProfile.compose(stageIdentifier));
    }
}