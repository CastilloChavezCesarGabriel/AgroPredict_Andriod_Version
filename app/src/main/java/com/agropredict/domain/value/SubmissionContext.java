package com.agropredict.domain.value;

import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.CropImage;

public final class SubmissionContext {
    private final Crop crop;
    private final CropImage image;

    public SubmissionContext(Crop crop, CropImage image) {
        this.crop = crop;
        this.image = image;
    }

    public void accept(ISubmissionContextVisitor visitor) {
        visitor.visit(crop, image);
    }
}
