package com.agropredict.domain.entity;

import com.agropredict.domain.component.crop.Field;
import com.agropredict.domain.component.crop.GrowthCycle;
import com.agropredict.domain.component.crop.Soil;
import com.agropredict.domain.visitor.crop.ICropVisitor;

public final class Crop {
    private final String identifier;
    private final String cropType;
    private Field field;
    private Soil soil;
    private GrowthCycle growthCycle;

    public Crop(String identifier, String cropType) {
        this.identifier = identifier;
        this.cropType = cropType;
    }

    public void locate(String fieldName, String location) {
        this.field = new Field(fieldName, location);
    }

    public void plant(String soilType, String area) {
        this.soil = new Soil(soilType, area);
    }

    public void schedule(String plantingDate, String stageIdentifier) {
        this.growthCycle = new GrowthCycle(plantingDate, stageIdentifier);
    }

    public void accept(ICropVisitor visitor) {
        visitor.visitIdentity(identifier, cropType);
        if (field != null) field.accept(visitor);
        if (soil != null) soil.accept(visitor);
        if (growthCycle != null) growthCycle.accept(visitor);
    }
}