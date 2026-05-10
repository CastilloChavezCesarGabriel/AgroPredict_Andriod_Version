package com.agropredict.domain.crop;

import com.agropredict.domain.crop.visitor.IPlantingConsumer;

public final class GrowthCycle {
    private final String plantingDate;
    private final String stageIdentifier;

    public GrowthCycle(String plantingDate, String stageIdentifier) {
        this.plantingDate = plantingDate;
        this.stageIdentifier = stageIdentifier;
    }

    public void track(IPlantingConsumer consumer) {
        consumer.track(plantingDate, stageIdentifier);
    }
}