package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropContentVisitor;

public final class CropContent {
    private final CropEnvironment environment;
    private final CropOwnership ownership;

    public CropContent(CropEnvironment environment, CropOwnership ownership) {
        this.environment = environment;
        this.ownership = ownership;
    }

    public boolean isComplete() {
        return environment != null && ownership != null;
    }

    public void accept(ICropContentVisitor visitor) {
        visitor.visit(environment, ownership);
    }
}