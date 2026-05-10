package com.agropredict.infrastructure.image_classification;

import com.agropredict.application.service.IImageClassifier;
import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import java.nio.ByteBuffer;

public final class TFLiteClassifier implements IImageClassifier {
    private final TFLiteModel model;
    private final ImageProcessor processor;

    public TFLiteClassifier(TFLiteModel model, ImageProcessor processor) {
        this.model = model;
        this.processor = processor;
    }

    @Override
    public void classify(String imagePath, IClassificationResult visitor) {
        ByteBuffer input = processor.prepare(imagePath, visitor);
        if (input != null) model.infer(input, visitor);
    }
}
