package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropVisitor;

public final class GrowthCycle {
    private final String plantingDate;
    private final String stageIdentifier;

    public GrowthCycle(String plantingDate, String stageIdentifier) {
        this.plantingDate = plantingDate;
        this.stageIdentifier = stageIdentifier;
    }

    public void accept(ICropVisitor visitor) {
        visitor.visitPlanting(plantingDate, stageIdentifier);
    }
}
