package com.agropredict.domain.entity;

import com.agropredict.domain.IIdentifierConsumer;
import com.agropredict.domain.component.diagnostic.Assessment;
import com.agropredict.domain.component.diagnostic.CropImage;
import com.agropredict.domain.component.diagnostic.ISeverityVisitor;
import com.agropredict.domain.component.diagnostic.Prediction;
import com.agropredict.domain.component.diagnostic.Recommendation;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticPairVisitor;
import com.agropredict.domain.visitor.diagnostic.IDiagnosticVisitor;

public final class Diagnostic {
    private final String identifier;
    private final Prediction prediction;
    private Assessment assessment;
    private CropImage cropImage;

    public Diagnostic(String identifier, Prediction prediction) {
        this.identifier = identifier;
        this.prediction = prediction;
    }

    public void conclude(String severity, Recommendation recommendation) {
        this.assessment = new Assessment(severity, recommendation);
    }

    public void attribute(String cropIdentifier, String imageIdentifier) {
        this.cropImage = new CropImage(cropIdentifier, imageIdentifier);
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
        if (cropImage != null) cropImage.accept(visitor);
        prediction.accept(visitor);
        if (assessment != null) assessment.accept(visitor);
    }

    public void pair(String otherIdentifier, IDiagnosticPairVisitor visitor) {
        visitor.match(identifier, otherIdentifier);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.accept(identifier);
    }
}
