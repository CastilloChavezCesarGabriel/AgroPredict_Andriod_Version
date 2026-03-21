package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropDetailVisitor;

public final class CropDetail {
    private final String cropType;
    private final String fieldName;

    public CropDetail(String cropType, String fieldName) {
        this.cropType = cropType;
        this.fieldName = fieldName;
    }

    public void accept(ICropDetailVisitor visitor) {
        visitor.visit(cropType, fieldName);
    }
}