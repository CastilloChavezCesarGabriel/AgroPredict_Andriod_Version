package com.agropredict.domain.diagnostic;

import com.agropredict.domain.diagnostic.visitor.IDiagnosticTargetConsumer;
import com.agropredict.domain.guard.ArgumentPrecondition;

public final class DiagnosticTarget implements IDiagnosticTarget {
    private final String cropIdentifier;
    private final String imageIdentifier;

    public DiagnosticTarget(String cropIdentifier, String imageIdentifier) {
        this.cropIdentifier = ArgumentPrecondition.validate(cropIdentifier, "diagnostic target crop identifier");
        this.imageIdentifier = ArgumentPrecondition.validate(imageIdentifier, "diagnostic target image identifier");
    }

    @Override
    public void bind(IDiagnosticTargetConsumer consumer) {
        consumer.bind(cropIdentifier, imageIdentifier);
    }
}