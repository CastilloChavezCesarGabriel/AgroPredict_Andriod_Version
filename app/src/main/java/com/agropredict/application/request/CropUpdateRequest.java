package com.agropredict.application.request;

import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.visitor.crop.ICropVisitor;

public final class CropUpdateRequest {
    private final Crop crop;

    public CropUpdateRequest(Crop crop) {
        this.crop = crop;
    }

    public void accept(ICropVisitor visitor) {
        crop.accept(visitor);
    }
}