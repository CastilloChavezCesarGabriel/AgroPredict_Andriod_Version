package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticConditionsVisitor;

public final class DiagnosticConditions {
    private final DiagnosticContext context;
    private final DiagnosticEnvironment environment;

    public DiagnosticConditions(DiagnosticContext context, DiagnosticEnvironment environment) {
        this.context = context;
        this.environment = environment;
    }

    public void accept(IDiagnosticConditionsVisitor visitor) {
        visitor.visit(context, environment);
    }
}