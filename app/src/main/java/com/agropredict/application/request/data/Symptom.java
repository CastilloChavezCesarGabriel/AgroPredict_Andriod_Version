package com.agropredict.application.request.data;

import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class Symptom {
    private final String symptomType;
    private final String severity;

    public Symptom(String symptomType, String severity) {
        this.symptomType = symptomType;
        this.severity = severity;
    }

    public void accept(IQuestionnaireVisitor visitor) {
        visitor.visitSymptom(symptomType, severity);
    }
}