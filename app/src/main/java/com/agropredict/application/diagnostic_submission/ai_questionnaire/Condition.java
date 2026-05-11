package com.agropredict.application.diagnostic_submission.ai_questionnaire;

import com.agropredict.application.IAnswerConsumer;
import java.util.Objects;

public final class Condition {
    private final Weather environment;
    private final SoilAnswer soil;

    public Condition(Weather environment, SoilAnswer soil) {
        this.environment = Objects.requireNonNull(environment, "condition requires weather");
        this.soil = Objects.requireNonNull(soil, "condition requires a soil answer");
    }

    public void accept(IAnswerConsumer consumer) {
        environment.accept(consumer);
        soil.accept(consumer);
    }
}