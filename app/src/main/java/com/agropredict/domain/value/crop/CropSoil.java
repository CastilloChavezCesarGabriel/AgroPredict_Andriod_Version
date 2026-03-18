package com.agropredict.domain.value.crop;

import com.agropredict.domain.visitor.ICropSoilVisitor;

public final class CropSoil {
    private final String soilTypeIdentifier;
    private final String area;

    public CropSoil(String soilTypeIdentifier, String area) {
        this.soilTypeIdentifier = soilTypeIdentifier;
        this.area = area;
    }

    public void accept(ICropSoilVisitor visitor) {
        visitor.visitSoil(soilTypeIdentifier, area);
    }
}