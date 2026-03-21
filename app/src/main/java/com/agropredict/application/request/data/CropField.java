package com.agropredict.application.request.data;

import com.agropredict.domain.component.crop.CropContent;
import com.agropredict.domain.component.crop.CropEnvironment;
import com.agropredict.domain.component.crop.CropOwnership;
import com.agropredict.domain.component.crop.CropSoil;

public final class CropField {
    private final String soilType;
    private final String stage;

    public CropField(String soilType, String stage) {
        this.soilType = soilType;
        this.stage = stage;
    }

    public CropContent produce() {
        CropSoil soil = new CropSoil(soilType, null);
        CropEnvironment environment = new CropEnvironment(null, soil);
        CropOwnership ownership = new CropOwnership(null, stage);
        return new CropContent(environment, ownership);
    }
}