package com.agropredict.domain.visitor;

import com.agropredict.domain.value.crop.CropData;

public interface ICropVisitor {
    void visit(String identifier, CropData data);
}