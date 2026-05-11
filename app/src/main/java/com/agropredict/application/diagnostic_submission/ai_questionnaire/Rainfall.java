package com.agropredict.application.diagnostic_submission.ai_questionnaire;

import com.agropredict.application.IAnswerConsumer;
import java.util.Objects;

public final class Rainfall {
    private final String precipitation;

    public Rainfall(String precipitation) {
        this.precipitation = Objects.requireNonNull(precipitation, "rainfall requires a precipitation answer");
    }

    public void accept(IAnswerConsumer consumer) {
        AnswerKey.RAIN.pair(consumer, precipitation);
    }
}