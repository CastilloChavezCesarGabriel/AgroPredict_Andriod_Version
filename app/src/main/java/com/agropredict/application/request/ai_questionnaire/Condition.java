package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Condition {
    private final Weather environment;
    private final SoilAnswer soil;

    public Condition(Weather environment, SoilAnswer soil) {
        this.environment = environment;
        this.soil = soil;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        environment.accept(visitor);
        soil.accept(visitor);
    }
}