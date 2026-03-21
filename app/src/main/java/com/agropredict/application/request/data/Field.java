package com.agropredict.application.request.data;

import com.agropredict.application.request.input.Cultivation;
import com.agropredict.application.request.input.Photograph;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.CropImage;

public final class Field {
    private final Cultivation crop;
    private final Photograph image;

    public Field(Cultivation crop, Photograph image) {
        this.crop = crop;
        this.image = image;
    }

    public Crop cultivate() {
        return crop.cultivate();
    }

    public CropImage capture() {
        return image.archive();
    }
}