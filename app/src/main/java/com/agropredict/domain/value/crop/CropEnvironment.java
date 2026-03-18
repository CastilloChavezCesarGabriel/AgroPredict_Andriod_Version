package com.agropredict.domain.value.crop;

import com.agropredict.domain.visitor.ICropEnvironmentVisitor;

public final class CropEnvironment {
    private final CropLocation location;
    private final CropSoil soil;

    public CropEnvironment(CropLocation location, CropSoil soil) {
        this.location = location;
        this.soil = soil;
    }

    public void accept(ICropEnvironmentVisitor visitor) {
        visitor.visit(location, soil);
    }
}