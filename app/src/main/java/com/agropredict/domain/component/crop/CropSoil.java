package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropSoilVisitor;

public final class CropSoil {
    private final String soilTypeIdentifier;
    private final String area;

    public CropSoil(String soilTypeIdentifier, String area) {
        this.soilTypeIdentifier = soilTypeIdentifier;
        this.area = area;
    }

    public boolean isSurveyed() {
        return soilTypeIdentifier != null && !soilTypeIdentifier.isEmpty();
    }

    public boolean isMeasured() {
        return area != null && !area.isEmpty();
    }

    public void accept(ICropSoilVisitor visitor) {
        visitor.visitSoil(soilTypeIdentifier, area);
    }
}