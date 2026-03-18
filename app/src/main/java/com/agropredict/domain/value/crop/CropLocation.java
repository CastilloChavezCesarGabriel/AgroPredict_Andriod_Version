package com.agropredict.domain.value.crop;

import com.agropredict.domain.visitor.ICropLocationVisitor;

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