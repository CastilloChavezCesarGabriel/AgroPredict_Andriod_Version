package com.agropredict.application.request.ai_questionnaire;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class CropCare {
    private final FarmManagement management;
    private final Observation observation;

    public CropCare(FarmManagement management, Observation observation) {
        this.management = management;
        this.observation = observation;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        management.accept(visitor);
        observation.accept(visitor);
    }
}