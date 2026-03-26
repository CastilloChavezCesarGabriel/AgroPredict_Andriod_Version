package com.agropredict.application.request.input;

import com.agropredict.domain.Identifier;
import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.component.crop.CropData;
import com.agropredict.domain.component.crop.CropDetail;

public final class Cultivation {
    private final String predictedCrop;
    private final String stage;

    public Cultivation(String predictedCrop, String stage) {
        this.predictedCrop = predictedCrop;
        this.stage = stage;
    }

    public Crop cultivate() {
        String identifier = Identifier.generate("crop");
        CropDetail detail = new CropDetail(predictedCrop, stage);
        return Crop.create(identifier, new CropData(detail, null));
    }
}