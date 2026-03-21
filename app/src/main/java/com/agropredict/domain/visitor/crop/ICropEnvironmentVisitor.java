package com.agropredict.domain.visitor.crop;

import com.agropredict.domain.component.crop.CropLocation;
import com.agropredict.domain.component.crop.CropSoil;

public interface ICropEnvironmentVisitor {
    void visit(CropLocation location, CropSoil soil);
}