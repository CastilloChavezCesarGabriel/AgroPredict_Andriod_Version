package com.agropredict.infrastructure.image_classification;

import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.visitor.IClassificationResultVisitor;
import java.nio.ByteBuffer;

public final class TFLiteClassifier implements IImageClassifier {
    private final TFLiteModel model;
    private final ImageProcessor processor;

    public TFLiteClassifier(TFLiteModel model, ImageProcessor processor) {
        this.model = model;
        this.processor = processor;
    }

    @Override
    public void classify(String imagePath, IClassificationResultVisitor consumer) {
        ByteBuffer input = processor.prepare(imagePath, consumer);
        if (input != null) model.infer(input, consumer);
    }
}
