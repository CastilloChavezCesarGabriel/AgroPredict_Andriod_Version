package com.agropredict.domain.diagnostic.visitor;

public interface IDiagnosticTargetConsumer {
    void bind(String cropIdentifier, String imageIdentifier);
}