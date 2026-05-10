package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.service.IImageClassifier;
import com.agropredict.domain.diagnostic.ConfidentClassification;
import com.agropredict.domain.diagnostic.UnconfidentClassification;
import com.agropredict.visitor.ConfidentExpecter;
import com.agropredict.visitor.UnconfidentExpecter;

import org.junit.Test;

public final class ClassifyImageUseCaseTest {
    private IImageClassifier tomatoClassifier() {
        return (path, visitor) -> new ConfidentClassification("Tomato", 0.9).accept(visitor);
    }

    private IImageClassifier rejectingClassifier(String reason) {
        return (path, visitor) -> new UnconfidentClassification(reason).accept(visitor);
    }

    @Test
    public void testClassifyValidImage() {
        new ClassifyImageUseCase(tomatoClassifier())
            .classify("/path/image.jpg", new ConfidentExpecter("Tomato", 0.9));
    }

    @Test
    public void testClassifyInvalidImage() {
        new ClassifyImageUseCase(rejectingClassifier("File too large"))
            .classify("/path/huge.jpg", new UnconfidentExpecter("File too large"));
    }

    @Test
    public void testClassifyLowConfidence() {
        new ClassifyImageUseCase(rejectingClassifier("Could not identify the crop with certainty"))
            .classify("/path/blurry.jpg", new UnconfidentExpecter(null));
    }
}
