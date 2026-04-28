package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropVisitor;

public final class CropProfile {
    private final Field field;
    private final Soil soil;
    private final GrowthCycle growthCycle;

    public CropProfile(Field field, Soil soil, GrowthCycle growthCycle) {
        this.field = field;
        this.soil = soil;
        this.growthCycle = growthCycle;
    }

    public void accept(ICropVisitor visitor) {
        if (field != null) field.accept(visitor);
        if (soil != null) soil.accept(visitor);
        if (growthCycle != null) growthCycle.accept(visitor);
    }
}