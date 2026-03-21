package com.agropredict.application.request.data;

import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.component.crop.CropData;
import com.agropredict.domain.component.crop.CropDetail;

public final class CropIdentity {
    private final String identifier;
    private final String cropName;

    public CropIdentity(String identifier, String cropName) {
        this.identifier = identifier;
        this.cropName = cropName;
    }

    public Crop form(CropField field) {
        CropDetail detail = new CropDetail(null, cropName);
        CropData data = new CropData(detail, field.produce());
        return Crop.create(identifier, data);
    }
}