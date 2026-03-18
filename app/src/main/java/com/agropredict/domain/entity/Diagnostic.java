package com.agropredict.domain.entity;

import com.agropredict.domain.value.diagnostic.DiagnosticData;
import com.agropredict.domain.visitor.IDiagnosticVisitor;

public final class Diagnostic {
    private final String identifier;
    private final DiagnosticData data;

    private Diagnostic(String identifier, DiagnosticData data) {
        this.identifier = identifier;
        this.data = data;
    }

    public static Diagnostic create(String identifier, DiagnosticData data) {
        return new Diagnostic(identifier, data);
    }

    public void accept(IDiagnosticVisitor visitor) {
        visitor.visit(identifier, data);
    }

    public boolean isConfident() {
        return data.isConfident();
    }
}