package com.agropredict.domain.visitor;

import com.agropredict.domain.value.crop.CropLocation;
import com.agropredict.domain.value.crop.CropSoil;

public interface ICropEnvironmentVisitor {
    void visit(CropLocation location, CropSoil soil);
}