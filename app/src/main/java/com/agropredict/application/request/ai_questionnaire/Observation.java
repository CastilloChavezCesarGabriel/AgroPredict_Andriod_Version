package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IAnswerConsumer;
import java.util.Objects;

public final class Observation {
    private final Symptom symptom;
    private final Pest pest;

    public Observation(Symptom symptom, Pest pest) {
        this.symptom = Objects.requireNonNull(symptom, "observation requires a symptom");
        this.pest = Objects.requireNonNull(pest, "observation requires a pest answer");
    }

    public void accept(IAnswerConsumer consumer) {
        symptom.accept(consumer);
        pest.accept(consumer);
    }
}