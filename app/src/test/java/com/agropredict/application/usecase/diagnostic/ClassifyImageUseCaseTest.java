package com.agropredict.application.usecase.diagnostic;

import static org.junit.Assert.assertTrue;

import com.agropredict.application.operation_result.ClassificationResult;
import com.agropredict.application.service.IImageService;
import com.agropredict.application.visitor.IClassificationResultVisitor;
import com.agropredict.visitor.TestClassificationResultVisitor;

import org.junit.Test;

public final class ClassifyImageUseCaseTest {

    private IImageService fakeImage(String validationError, String crop, double confidence) {
        return new IImageService() {
            @Override public void classify(String path, IClassificationResultVisitor consumer) {
                if (validationError != null) {
                    consumer.reject(validationError);
                    return;
                }
                new ClassificationResult(crop, confidence).accept(consumer);
            }
            @Override public String compress(String uri) { return uri; }
        };
    }

    @Test
    public void testClassifyValidImage() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassifyImageUseCase(fakeImage(null, "Tomato", 0.9)).classify("/path/image.jpg", visitor);
        assertTrue(visitor.isAccepted("Tomato"));
    }

    @Test
    public void testClassifyInvalidImage() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassifyImageUseCase(fakeImage("File too large", "Tomato", 0.9)).classify("/path/huge.jpg", visitor);
        assertTrue(visitor.isRejected("File too large"));
    }

    @Test
    public void testClassifyLowConfidence() {
        TestClassificationResultVisitor visitor = new TestClassificationResultVisitor();
        new ClassifyImageUseCase(fakeImage(null, "Unknown", 0.3)).classify("/path/blurry.jpg", visitor);
        assertTrue(visitor.wasRejected());
    }
}
