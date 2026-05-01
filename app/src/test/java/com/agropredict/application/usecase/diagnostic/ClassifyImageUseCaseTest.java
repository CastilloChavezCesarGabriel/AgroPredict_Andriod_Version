package com.agropredict.application.usecase.diagnostic;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.operation_result.ClassificationResult;
import com.agropredict.application.service.IImageClassifier;
import com.agropredict.visitor.TestClassificationResultVisitor;

import org.junit.Test;

public final class ClassifyImageUseCaseTest {

    private IImageClassifier fakeClassifier(String validationError, String crop, double confidence) {
        return (path, consumer) -> {
            if (validationError != null) {
                consumer.onReject(validationError);
                return;
            }
            new ClassificationResult(crop, confidence).accept(consumer);
        };
    }

    @Test
    public void testClassifyValidImage() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassifyImageUseCase(fakeClassifier(null, "Tomato", 0.9)).classify("/path/image.jpg", visitor);
        assertTrue(visitor.isAccepted("Tomato"));
    }

    @Test
    public void testClassifyInvalidImage() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassifyImageUseCase(fakeClassifier("File too large", "Tomato", 0.9)).classify("/path/huge.jpg", visitor);
        assertTrue(visitor.isRejected("File too large"));
    }

    @Test
    public void testClassifyLowConfidence() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassifyImageUseCase(fakeClassifier(null, "Unknown", 0.3)).classify("/path/blurry.jpg", visitor);
        assertTrue(visitor.wasRejected());
    }
}