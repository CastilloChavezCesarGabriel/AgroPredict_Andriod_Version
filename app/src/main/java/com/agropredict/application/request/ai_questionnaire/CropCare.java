package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IAnswerConsumer;
import java.util.Objects;

public final class CropCare {
    private final FarmManagement management;
    private final Observation observation;

    public CropCare(FarmManagement management, Observation observation) {
        this.management = Objects.requireNonNull(management, "crop care requires a farm management answer");
        this.observation = Objects.requireNonNull(observation, "crop care requires an observation");
    }

    public void accept(IAnswerConsumer consumer) {
        management.accept(consumer);
        observation.accept(consumer);
    }
}