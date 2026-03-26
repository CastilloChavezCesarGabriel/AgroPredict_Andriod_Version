package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropLocationVisitor;

public final class CropLocation {
    private final String location;
    private final String plantingDate;

    public CropLocation(String location, String plantingDate) {
        this.location = location;
        this.plantingDate = plantingDate;
    }

    public void accept(ICropLocationVisitor visitor) {
        visitor.visitLocation(location, plantingDate);
    }
}