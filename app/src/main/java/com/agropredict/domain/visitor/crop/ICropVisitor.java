package com.agropredict.domain.visitor.crop;

import com.agropredict.domain.component.crop.CropData;

public interface ICropVisitor {
    void visit(String identifier, CropData data);
}