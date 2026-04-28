package com.agropredict.infrastructure.image_classification;

import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.visitor.IClassificationResultVisitor;
import java.nio.ByteBuffer;

public final class TFLiteClassifier implements IImageClassifier {
    private final TFLiteModel model;
    private final ImageValidator validator;
    private final ImagePreprocessor preprocessor;

    public TFLiteClassifier(TFLiteModel model, ImageValidator validator, ImagePreprocessor preprocessor) {
        this.model = model;
        this.validator = validator;
        this.preprocessor = preprocessor;
    }

    @Override
    public void classify(String imagePath, IClassificationResultVisitor consumer) {
        String error = validator.validate(imagePath);
        if (error != null) { consumer.reject(error); return; }
        ByteBuffer input = preprocessor.prepare(imagePath);
        if (input == null) { consumer.reject("Could not process image"); return; }
        model.infer(input, consumer);
    }
}