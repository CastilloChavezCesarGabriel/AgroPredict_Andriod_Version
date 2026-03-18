package com.agropredict.domain.value.crop;

import com.agropredict.domain.visitor.ICropDataVisitor;

public final class CropData {
    private final CropDetail detail;
    private final CropContent content;

    public CropData(CropDetail detail, CropContent content) {
        this.detail = detail;
        this.content = content;
    }

    public void accept(ICropDataVisitor visitor) {
        visitor.visit(detail, content);
    }
}