package com.agropredict.infrastructure.image_classification;

import com.agropredict.application.service.IImageClassifier;
import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import com.agropredict.infrastructure.image_classification.preprocessing.ImageProcessor;

import java.nio.ByteBuffer;
import java.util.Objects;

public final class TFLiteClassifier implements IImageClassifier {
    private final TFLiteModel model;
    private final ImageProcessor processor;

    public TFLiteClassifier(TFLiteModel model, ImageProcessor processor) {
        this.model = Objects.requireNonNull(model, "tflite classifier requires a model");
        this.processor = Objects.requireNonNull(processor, "tflite classifier requires a processor");
    }

    @Override
    public void classify(String imagePath, IClassificationResult visitor) {
        ByteBuffer preparedBuffer = processor.prepare(imagePath, visitor);
        if (preparedBuffer != null) model.infer(preparedBuffer, visitor);
    }
}
