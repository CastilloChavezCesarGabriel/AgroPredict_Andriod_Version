package com.agropredict.domain.value.diagnostic;

import com.agropredict.domain.visitor.IDiagnosticDataVisitor;

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
}