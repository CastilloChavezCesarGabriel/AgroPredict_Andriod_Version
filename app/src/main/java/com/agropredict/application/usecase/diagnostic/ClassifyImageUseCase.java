package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.visitor.IClassificationResultVisitor;
import com.agropredict.application.service.IImageService;

public final class ClassifyImageUseCase {
    private final IImageService imageService;

    public ClassifyImageUseCase(IImageService imageService) {
        this.imageService = imageService;
    }

    public void classify(String imagePath, IClassificationResultVisitor consumer) {
        imageService.classify(imagePath, consumer);
    }
}
