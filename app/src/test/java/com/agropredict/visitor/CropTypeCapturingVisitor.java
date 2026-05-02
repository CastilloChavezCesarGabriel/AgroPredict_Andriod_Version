package com.agropredict.visitor;

import com.agropredict.domain.visitor.crop.ICropVisitor;

public final class CropTypeCapturingVisitor implements ICropVisitor {
    private String cropType;

    @Override
    public void visitIdentity(String identifier, String cropType) {
        this.cropType = cropType;
    }

    public boolean wasClassifiedAs(String expected) {
        return expected != null && expected.equals(cropType);
    }
}