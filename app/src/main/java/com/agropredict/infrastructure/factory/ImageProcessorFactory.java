package com.agropredict.infrastructure.factory;

import com.agropredict.application.factory.IImageRejectionFactory;
import com.agropredict.infrastructure.image_classification.preprocessing.ImagePreprocessor;
import com.agropredict.infrastructure.image_classification.preprocessing.ImageProcessor;
import java.util.Objects;

public final class ImageProcessorFactory {
    private static final int MODEL_INPUT_SIZE = 224;
    private final IImageRejectionFactory rejectionFactory;

    public ImageProcessorFactory(IImageRejectionFactory rejectionFactory) {
        this.rejectionFactory = Objects.requireNonNull(rejectionFactory,
                "image processor factory requires a rejection factory");
    }

    public ImageProcessor create() {
        return new ImageProcessor(
                new ImagePreprocessor(MODEL_INPUT_SIZE),
                rejectionFactory.createProcessingFailed());
    }
}