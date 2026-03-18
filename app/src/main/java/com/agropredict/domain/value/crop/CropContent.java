package com.agropredict.domain.value.crop;

import com.agropredict.domain.visitor.ICropContentVisitor;

public final class CropContent {
    private final CropEnvironment environment;
    private final CropOwnership ownership;

    public CropContent(CropEnvironment environment, CropOwnership ownership) {
        this.environment = environment;
        this.ownership = ownership;
    }

    public void accept(ICropContentVisitor visitor) {
        visitor.visit(environment, ownership);
    }
}