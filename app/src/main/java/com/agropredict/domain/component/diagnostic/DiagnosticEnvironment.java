package com.agropredict.domain.component.diagnostic;

import com.agropredict.domain.visitor.diagnostic.IDiagnosticEnvironmentVisitor;

public final class DiagnosticEnvironment {
    private final double temperature;
    private final double humidity;

    public DiagnosticEnvironment(double temperature, double humidity) {
        this.temperature = temperature;
        this.humidity = humidity;
    }

    public void accept(IDiagnosticEnvironmentVisitor visitor) {
        visitor.visitEnvironment(temperature, humidity);
    }
}