package com.agropredict.domain.crop;

import com.agropredict.domain.crop.visitor.IPlantingConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;

public final class GrowthCycle {
    private final String plantingDate;
    private final String stageIdentifier;

    public GrowthCycle(String plantingDate, String stageIdentifier) {
        this.plantingDate = ArgumentPrecondition.validate(plantingDate, "growth cycle planting date");
        this.stageIdentifier = stageIdentifier;
    }

    public void track(IPlantingConsumer consumer) {
        consumer.track(plantingDate, stageIdentifier);
    }
}