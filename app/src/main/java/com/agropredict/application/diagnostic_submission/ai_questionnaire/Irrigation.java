package com.agropredict.application.diagnostic_submission.ai_questionnaire;

import com.agropredict.application.IAnswerConsumer;
import java.util.Objects;

public final class Irrigation {
    private final String practice;
    private final String fertilization;

    public Irrigation(String practice, String fertilization) {
        this.practice = Objects.requireNonNull(practice, "irrigation requires a practice answer");
        this.fertilization = Objects.requireNonNull(fertilization, "irrigation requires a fertilization answer");
    }

    public void accept(IAnswerConsumer consumer) {
        AnswerKey.IRRIGATION.pair(consumer, practice);
        AnswerKey.FERTILIZATION.pair(consumer, fertilization);
    }
}