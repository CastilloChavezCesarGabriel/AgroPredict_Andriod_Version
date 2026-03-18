package com.agropredict.domain.visitor;

public interface IDiagnosticEnvironmentVisitor {
    void visitEnvironment(double temperature, double humidity);
}