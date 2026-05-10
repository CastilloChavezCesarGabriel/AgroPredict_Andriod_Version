package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IAnswerConsumer;
import java.util.Objects;

public final class SoilAnswer {
    private final String moisture;
    private final String acidity;

    public SoilAnswer(String moisture, String acidity) {
        this.moisture = Objects.requireNonNull(moisture, "soil answer requires a moisture value");
        this.acidity = Objects.requireNonNull(acidity, "soil answer requires an acidity value");
    }

    public void accept(IAnswerConsumer consumer) {
        AnswerKey.SOIL_MOISTURE.pair(consumer, moisture);
        AnswerKey.PH.pair(consumer, acidity);
    }
}