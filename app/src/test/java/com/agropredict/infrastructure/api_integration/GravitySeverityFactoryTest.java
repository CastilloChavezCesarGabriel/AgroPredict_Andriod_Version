package com.agropredict.infrastructure.api_integration;

import static org.junit.Assert.assertTrue;

import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.domain.diagnostic.SeverityClassifier;
import com.agropredict.visitor.SeverityCapturingVisitor;

import org.junit.Test;

import java.util.List;

public final class GravitySeverityFactoryTest {
    private final ISeverityFactory factory = build();

    private static ISeverityFactory build() {
        SeverityClassifier healthy = new SeverityClassifier(List.of("bajo", "low"), new Severity("low", "Healthy", 0));
        SeverityClassifier moderate = new SeverityClassifier(List.of("moderado", "moderate"), new Severity("moderate", "Moderate issue", 1));
        SeverityClassifier severe = new SeverityClassifier(List.of("alto", "high", "critico", "critical"), new Severity("high", "Severe issue", 2));
        Severity unknown = new Severity(null, "Analysis complete", 0);
        return new GravitySeverityFactory(List.of(healthy, moderate, severe), unknown);
    }

    @Test
    public void testClassifyBajoYieldsHealthy() {
        Severity severity = factory.classify("Bajo");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedHealthy());
    }

    @Test
    public void testClassifyAltoYieldsSevere() {
        Severity severity = factory.classify("Alto");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedSevere());
    }

    @Test
    public void testClassifyModeradoYieldsModerate() {
        Severity severity = factory.classify("Moderado");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedModerate());
    }

    @Test
    public void testClassifyUnknownGravityFallsBackToUnknown() {
        Severity severity = factory.classify("GarbageInput");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedUnknown());
    }

    @Test
    public void testClassifyEmptyFallsBackToUnknown() {
        Severity severity = factory.classify("");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedUnknown());
    }
}
