package com.agropredict.application.request;

import com.agropredict.application.request.data.CropField;
import com.agropredict.application.request.data.CropIdentity;
import com.agropredict.domain.entity.Crop;

public final class CropUpdateRequest {
    private final CropIdentity identity;
    private final CropField field;

    public CropUpdateRequest(CropIdentity identity, CropField field) {
        this.identity = identity;
        this.field = field;
    }

    public Crop apply() {
        return identity.form(field);
    }
}