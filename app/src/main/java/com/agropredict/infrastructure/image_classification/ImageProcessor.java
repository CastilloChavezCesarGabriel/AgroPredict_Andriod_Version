package com.agropredict.infrastructure.image_classification;

import com.agropredict.domain.diagnostic.UnconfidentClassification;
import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import java.nio.ByteBuffer;

public final class ImageProcessor {
    private final ImageValidator validator;
    private final ImagePreprocessor preprocessor;

    public ImageProcessor(ImageValidator validator, ImagePreprocessor preprocessor) {
        this.validator = validator;
        this.preprocessor = preprocessor;
    }

    public ByteBuffer prepare(String imagePath, IClassificationResult visitor) {
        String error = validator.validate(imagePath);
        if (error != null) {
            new UnconfidentClassification(error).accept(visitor);
            return null;
        }
        ByteBuffer input = preprocessor.prepare(imagePath);
        if (input == null) new UnconfidentClassification("Could not process image").accept(visitor);
        return input;
    }
}
