package com.agropredict.domain.visitor;

import com.agropredict.domain.value.crop.CropContent;
import com.agropredict.domain.value.crop.CropDetail;

public interface ICropDataVisitor {
    void visit(CropDetail detail, CropContent content);
}