package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticDataVisitor;

public final class DiagnosticData {
    private final Prediction prediction;
    private final DiagnosticContent content;

    public DiagnosticData(Prediction prediction, DiagnosticContent content) {
        this.prediction = prediction;
        this.content = content;
    }

    public void accept(IDiagnosticDataVisitor visitor) {
        visitor.visit(prediction, content);
    }

    public boolean isConfident() {
        return prediction.isConfident();
    }

    public boolean isSevere() {
        return content != null && content.isSevere();
    }
}