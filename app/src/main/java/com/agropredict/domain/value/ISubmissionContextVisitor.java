package com.agropredict.domain.value;

import com.agropredict.domain.entity.Crop;
import com.agropredict.domain.entity.CropImage;

public interface ISubmissionContextVisitor {
    void visit(Crop crop, CropImage image);
}
