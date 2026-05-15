package com.agropredict.infrastructure.api_integration;

import static org.junit.Assert.assertTrue;

import com.agropredict.domain.diagnostic.severity.GravitySeverityResolver;
import com.agropredict.domain.diagnostic.severity.HealthySeverity;
import com.agropredict.domain.diagnostic.severity.ISeverity;
import com.agropredict.domain.diagnostic.severity.ISeverityResolver;
import com.agropredict.domain.diagnostic.severity.ModerateSeverity;
import com.agropredict.domain.diagnostic.severity.SevereSeverity;
import com.agropredict.domain.diagnostic.severity.SeverityClassifier;
import com.agropredict.domain.diagnostic.severity.UnknownSeverity;
import com.agropredict.visitor.SeverityCapturingVisitor;

import org.junit.Test;

import java.util.List;

public final class GravitySeverityResolverTest {
    private final ISeverityResolver resolver = build();

    private static ISeverityResolver build() {
        SeverityClassifier healthy = new SeverityClassifier(List.of("bajo", "low"),
                new HealthySeverity(() -> "Healthy"));
        SeverityClassifier moderate = new SeverityClassifier(List.of("moderado", "moderate"),
                new ModerateSeverity(() -> "Moderate issue"));
        SeverityClassifier severe = new SeverityClassifier(List.of("alto", "high", "critico", "critical"),
                new SevereSeverity(() -> "Severe issue"));
        ISeverity unknown = new UnknownSeverity(() -> "Analysis complete");
        return new GravitySeverityResolver(List.of(healthy, moderate, severe), unknown);
    }

    @Test
    public void testClassifyBajoYieldsHealthy() {
        ISeverity severity = resolver.classify("Bajo");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedHealthy());
    }

    @Test
    public void testClassifyAltoYieldsSevere() {
        ISeverity severity = resolver.classify("Alto");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedSevere());
    }

    @Test
    public void testClassifyModeradoYieldsModerate() {
        ISeverity severity = resolver.classify("Moderado");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedModerate());
    }

    @Test
    public void testClassifyUnknownGravityFallsBackToUnknown() {
        ISeverity severity = resolver.classify("GarbageInput");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedUnknown());
    }

    @Test
    public void testClassifyEmptyFallsBackToUnknown() {
        ISeverity severity = resolver.classify("");
        SeverityCapturingVisitor visitor = new SeverityCapturingVisitor();
        severity.label(visitor);
        assertTrue(visitor.recordedUnknown());
    }
}
