package com.agropredict.application.request.diagnostic_submission;

import com.agropredict.application.diagnostic_submission.Allocation;
import com.agropredict.application.diagnostic_submission.Cropland;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.Photograph;

public final class Subject {
    private final Cultivation cultivation;
    private final PhotographInput image;

    public Subject(Cultivation cultivation, PhotographInput image) {
        this.cultivation = cultivation;
        this.image = image;
    }

    public void store(Cropland cropland, Allocation allocation) {
        allocation.expose((cropIdentifier, imageIdentifier) -> arrange(cropland, cropIdentifier, imageIdentifier));
    }

    private void arrange(Cropland cropland, String cropIdentifier, String imageIdentifier) {
        Crop crop = cultivation.cultivate(cropIdentifier, cropland);
        cropland.persist(crop);
        Photograph photograph = image.capture(imageIdentifier);
        cropland.capture(photograph, crop);
    }
}
