package com.agropredict.domain.entity;

import com.agropredict.domain.value.crop.CropData;
import com.agropredict.domain.visitor.ICropVisitor;

public final class Crop {
    private final String identifier;
    private final CropData data;

    private Crop(String identifier, CropData data) {
        this.identifier = identifier;
        this.data = data;
    }

    public static Crop create(String identifier, CropData data) {
        return new Crop(identifier, data);
    }

    public void accept(ICropVisitor visitor) {
        visitor.visit(identifier, data);
    }
}