package com.agropredict.domain.visitor.diagnostic;

public interface IDiagnosticEnvironmentVisitor {
    void visitEnvironment(double temperature, double humidity);
}