package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropVisitor;

public final class Soil {
    private final String typeIdentifier;
    private final String area;

    public Soil(String typeIdentifier, String area) {
        this.typeIdentifier = typeIdentifier;
        this.area = area;
    }

    public void accept(ICropVisitor visitor) {
        visitor.visitSoil(typeIdentifier, area);
    }
}