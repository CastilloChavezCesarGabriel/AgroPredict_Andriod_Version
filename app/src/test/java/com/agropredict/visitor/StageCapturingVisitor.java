package com.agropredict.visitor;

import com.agropredict.domain.visitor.crop.ICropVisitor;

public final class StageCapturingVisitor implements ICropVisitor {
    private String stageIdentifier;

    @Override
    public void visitPlanting(String date, String stageIdentifier) {
        this.stageIdentifier = stageIdentifier;
    }

    public boolean recordedStage(String expected) {
        return expected != null && expected.equals(stageIdentifier);
    }
}
