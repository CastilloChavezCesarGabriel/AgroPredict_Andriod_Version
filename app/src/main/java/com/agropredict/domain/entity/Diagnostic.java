package com.agropredict.domain.entity;

import com.agropredict.domain.component.diagnostic.Assessment;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class Diagnostic {
    private final String identifier;
    private final Prediction prediction;
    private Assessment assessment;

    public Diagnostic(String identifier, Prediction prediction) {
        this.identifier = identifier;
        this.prediction = prediction;
    }

    public void conclude(String severity, String summary) {
        this.assessment = new Assessment(severity, summary);
    }

    public void recommend(String recommendation) {
        if (assessment != null) assessment.conclude(recommendation);
    }

    public boolean isConfident() {
        return prediction.isConfident();
    }

    public boolean isSevere() {
        return assessment != null && assessment.isSevere();
    }

    public String classify() {
        return assessment != null ? assessment.classify() : "Pending";
    }

    public void accept(IDiagnosticVisitor visitor) {
        visitor.visitIdentity(identifier);
        prediction.accept(visitor);
        if (assessment != null) assessment.accept(visitor);
    }
}