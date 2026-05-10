package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IAnswerConsumer;
import java.util.Objects;

public final class Symptom {
    private final String type;
    private final String severity;

    public Symptom(String type, String severity) {
        this.type = Objects.requireNonNull(type, "symptom requires a type");
        this.severity = Objects.requireNonNull(severity, "symptom requires a severity");
    }

    public void accept(IAnswerConsumer consumer) {
        AnswerKey.SYMPTOM.pair(consumer, type);
        AnswerKey.SEVERITY.pair(consumer, severity);
    }
}