package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.visitor.IClassificationResultVisitor;

public final class ClassifyImageUseCase {
    private final IImageClassifier classifier;

    public ClassifyImageUseCase(IImageClassifier classifier) {
        this.classifier = classifier;
    }

    public void classify(String imagePath, IClassificationResultVisitor consumer) {
        classifier.classify(imagePath, consumer);
    }
}