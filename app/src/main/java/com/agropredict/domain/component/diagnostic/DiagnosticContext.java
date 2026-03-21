package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticContextVisitor;

public final class DiagnosticContext {
    private final String cropIdentifier;
    private final String imageIdentifier;

    public DiagnosticContext(String cropIdentifier, String imageIdentifier) {
        this.cropIdentifier = cropIdentifier;
        this.imageIdentifier = imageIdentifier;
    }

    public void accept(IDiagnosticContextVisitor visitor) {
        visitor.visitContext(cropIdentifier, imageIdentifier);
    }
}