package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.IDiagnosticTargetConsumer;

public final class DiagnosticTarget {
    private final String cropIdentifier;
    private final String imageIdentifier;

    public DiagnosticTarget(String cropIdentifier, String imageIdentifier) {
        this.cropIdentifier = cropIdentifier;
        this.imageIdentifier = imageIdentifier;
    }

    public void bind(IDiagnosticTargetConsumer consumer) {
        consumer.bind(cropIdentifier, imageIdentifier);
    }
}