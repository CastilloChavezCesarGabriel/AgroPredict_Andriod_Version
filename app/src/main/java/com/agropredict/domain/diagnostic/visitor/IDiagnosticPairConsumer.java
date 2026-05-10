package com.agropredict.domain.diagnostic.visitor;

public interface IDiagnosticPairConsumer {
    void pair(String identifier, String otherIdentifier);
}