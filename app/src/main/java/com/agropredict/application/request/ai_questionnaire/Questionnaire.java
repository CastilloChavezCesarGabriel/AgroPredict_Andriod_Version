package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IAnswerConsumer;
import java.util.Objects;

public final class Questionnaire {
    private final Condition condition;
    private final CropCare cropCare;

    public Questionnaire(Condition condition, CropCare cropCare) {
        this.condition = Objects.requireNonNull(condition, "questionnaire requires a condition");
        this.cropCare = Objects.requireNonNull(cropCare, "questionnaire requires a crop care answer");
    }

    public void accept(IAnswerConsumer consumer) {
        condition.accept(consumer);
        cropCare.accept(consumer);
    }
}