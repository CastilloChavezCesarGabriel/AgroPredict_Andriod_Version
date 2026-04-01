package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Questionnaire {
    private final Condition condition;
    private final CropCare cropCare;

    public Questionnaire(Condition condition, CropCare cropCare) {
        this.condition = condition;
        this.cropCare = cropCare;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        condition.accept(visitor);
        cropCare.accept(visitor);
    }
}