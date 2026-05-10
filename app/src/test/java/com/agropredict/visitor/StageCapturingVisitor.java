package com.agropredict.visitor;

import com.agropredict.domain.crop.visitor.IPlantingConsumer;

public final class StageCapturingVisitor implements IPlantingConsumer {
    private String stageIdentifier;

    @Override
    public void track(String date, String stageIdentifier) {
        this.stageIdentifier = stageIdentifier;
    }

    public boolean recordedStage(String expected) {
        return expected != null && expected.equals(stageIdentifier);
    }
}
