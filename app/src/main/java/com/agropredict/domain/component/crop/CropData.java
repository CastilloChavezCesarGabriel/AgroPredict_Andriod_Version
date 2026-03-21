package com.agropredict.domain.component.crop;

import com.agropredict.domain.visitor.crop.ICropDataVisitor;

public final class CropData {
    private final CropDetail detail;
    private final CropContent content;

    public CropData(CropDetail detail, CropContent content) {
        this.detail = detail;
        this.content = content;
    }

    public boolean isComplete() {
        return detail != null && content != null && content.isComplete();
    }

    public void accept(ICropDataVisitor visitor) {
        visitor.visit(detail, content);
    }
}