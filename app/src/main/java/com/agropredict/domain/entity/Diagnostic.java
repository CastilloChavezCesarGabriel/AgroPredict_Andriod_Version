package com.agropredict.domain.entity;

import com.agropredict.domain.component.diagnostic.Assessment;
import com.agropredict.domain.component.diagnostic.ISeverityVisitor;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticPairVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class Diagnostic {
    private final String identifier;
    private final Prediction prediction;
    private Assessment assessment;

    public Diagnostic(String identifier, Prediction prediction) {
        this.identifier = identifier;
        this.prediction = prediction;
    }

    public void conclude(String severity, String summary, String recommendation) {
        this.assessment = new Assessment(severity, summary, recommendation);
    }

    public boolean isConfident() {
        return prediction.isConfident();
    }

    public void inspect(ISeverityVisitor visitor) {
        if (assessment == null) visitor.visit("Pending", 0);
        else assessment.inspect(visitor);
    }

    public void accept(IDiagnosticVisitor visitor) {
        visitor.visitIdentity(identifier);
        prediction.accept(visitor);
        if (assessment != null) assessment.accept(visitor);
    }

    public void pair(String otherIdentifier, IDiagnosticPairVisitor visitor) {
        visitor.match(identifier, otherIdentifier);
    }
}