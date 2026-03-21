package com.agropredict.application.request.data;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Condition {
    private final Weather environment;
    private final Soil soil;

    public Condition(Weather environment, Soil soil) {
        this.environment = environment;
        this.soil = soil;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        environment.accept(visitor);
        soil.accept(visitor);
    }
}