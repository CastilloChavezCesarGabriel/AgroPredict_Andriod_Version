package com.agropredict.visitor;

import com.agropredict.domain.crop.visitor.ICropIdentityConsumer;

public final class CropTypeCapturingVisitor implements ICropIdentityConsumer {
    private String cropType;

    @Override
    public void describe(String identifier, String cropType) {
        this.cropType = cropType;
    }

    public boolean wasClassifiedAs(String expected) {
        return expected != null && expected.equals(cropType);
    }
}
