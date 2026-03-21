package com.agropredict.domain.visitor.crop;

import com.agropredict.domain.component.crop.CropContent;
import com.agropredict.domain.component.crop.CropDetail;

public interface ICropDataVisitor {
    void visit(CropDetail detail, CropContent content);
}