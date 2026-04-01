package com.agropredict.application.operation_result;

import static org.junit.Assert.assertTrue;

import com.agropredict.visitor.TestClassificationResultVisitor;

import org.junit.Test;

public final class ClassificationResultTest {

    @Test
    public void testConfidentPrediction() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassificationResult("Tomato", 0.85).accept(visitor);
        assertTrue(visitor.isAccepted("Tomato"));
        assertTrue(visitor.isConfident(0.85));
    }

    @Test
    public void testLowConfidenceRejected() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassificationResult("Unknown", 0.3).accept(visitor);
        assertTrue(visitor.wasRejected());
    }

    @Test
    public void testExactThreshold() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassificationResult("Corn", 0.6).accept(visitor);
        assertTrue(visitor.isAccepted("Corn"));
    }

    @Test
    public void testJustBelowThreshold() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassificationResult("Corn", 0.59).accept(visitor);
        assertTrue(visitor.wasRejected());
    }

    @Test
    public void testZeroConfidence() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassificationResult("no_cultivo", 0.0).accept(visitor);
        assertTrue(visitor.wasRejected());
    }

    @Test
    public void testPerfectConfidence() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassificationResult("Wheat", 1.0).accept(visitor);
        assertTrue(visitor.isAccepted("Wheat"));
    }
}
