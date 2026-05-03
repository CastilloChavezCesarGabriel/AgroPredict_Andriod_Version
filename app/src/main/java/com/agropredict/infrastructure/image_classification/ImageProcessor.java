package com.agropredict.infrastructure.image_classification;

import com.agropredict.application.visitor.IClassificationResultVisitor;
import java.nio.ByteBuffer;

public final class ImageProcessor {
    private final ImageValidator validator;
    private final ImagePreprocessor preprocessor;

    public ImageProcessor(ImageValidator validator, ImagePreprocessor preprocessor) {
        this.validator = validator;
        this.preprocessor = preprocessor;
    }

    public ByteBuffer prepare(String imagePath, IClassificationResultVisitor consumer) {
        String error = validator.validate(imagePath);
        if (error != null) {
            consumer.onReject(error);
            return null;
        }
        ByteBuffer input = preprocessor.prepare(imagePath);
        if (input == null) consumer.onReject("Could not process image");
        return input;
    }
}
