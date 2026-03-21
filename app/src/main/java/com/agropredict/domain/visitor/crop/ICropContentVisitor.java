package com.agropredict.domain.visitor.crop;

import com.agropredict.domain.component.crop.CropEnvironment;
import com.agropredict.domain.component.crop.CropOwnership;

public interface ICropContentVisitor {
    void visit(CropEnvironment environment, CropOwnership ownership);
}