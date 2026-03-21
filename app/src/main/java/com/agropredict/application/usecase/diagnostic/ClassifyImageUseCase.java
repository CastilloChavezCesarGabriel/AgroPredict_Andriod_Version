package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.visitor.IClassificationResultVisitor;
import com.agropredict.application.result.ClassificationResult;
import com.agropredict.application.service.IImageService;

public final class ClassifyImageUseCase {
    private final IImageService imageService;

    public ClassifyImageUseCase(IImageService imageService) {
        this.imageService = imageService;
    }

    public void classify(String imagePath, IClassificationResultVisitor consumer) {
        String error = imageService.validate(imagePath);
        if (error != null) {
            consumer.reject(error);
            return;
        }
        ClassificationResult result = imageService.classify(imagePath);
        result.accept(consumer);
    }
}