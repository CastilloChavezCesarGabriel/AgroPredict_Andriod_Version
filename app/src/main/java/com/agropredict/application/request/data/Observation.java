package com.agropredict.application.request.data;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Observation {
    private final Symptom symptom;
    private final Pest pest;

    public Observation(Symptom symptom, Pest pest) {
        this.symptom = symptom;
        this.pest = pest;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        symptom.accept(visitor);
        pest.accept(visitor);
    }
}