package com.agropredict.domain.visitor;

import com.agropredict.domain.value.crop.CropEnvironment;
import com.agropredict.domain.value.crop.CropOwnership;

public interface ICropContentVisitor {
    void visit(CropEnvironment environment, CropOwnership ownership);
}