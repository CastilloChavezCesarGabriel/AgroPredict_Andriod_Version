package com.agropredict.infrastructure.api_integration;

import static org.junit.Assert.assertTrue;

import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.domain.diagnostic.Severity;
import com.agropredict.visitor.SeverityCapturingVisitor;

import org.junit.Test;

public final class GravitySeverityFactoryTest {
    private final ISeverityFactory factory = new GravitySeverityFactory();

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
