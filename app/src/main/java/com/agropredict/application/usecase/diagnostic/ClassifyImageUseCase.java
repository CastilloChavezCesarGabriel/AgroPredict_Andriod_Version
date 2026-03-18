package com.agropredict.application.usecase.diagnostic;

import com.agropredict.application.result.ClassificationResult;
import com.agropredict.application.service.IImageClassifierService;

public final class ClassifyImageUseCase {
    private final IImageClassifierService classifierService;

    public ClassifyImageUseCase(IImageClassifierService classifierService) {
        this.classifierService = classifierService;
    }

    public ClassificationResult classify(String imagePath) {
        return classifierService.classify(imagePath);
    }
}