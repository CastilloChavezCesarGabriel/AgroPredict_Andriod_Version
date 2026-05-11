package com.agropredict.application.diagnostic_submission.usecase;

import com.agropredict.application.service.IImageClassifier;
import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import java.util.Objects;

public final class ClassifyImageUseCase {
    private final IImageClassifier classifier;

    public ClassifyImageUseCase(IImageClassifier classifier) {
        this.classifier = Objects.requireNonNull(classifier, "classify image use case requires an image classifier");
    }

    public void classify(String imagePath, IClassificationResult visitor) {
        classifier.classify(imagePath, visitor);
    }
}
