package com.agropredict.infrastructure.image_classification.preprocessing;

import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;
import com.agropredict.domain.diagnostic.visitor.IClassificationResult;
import java.nio.ByteBuffer;
import java.util.Objects;

public final class ImageProcessor {
    private final ImagePreprocessor preprocessor;
    private final IImageRejection processingFailedRejection;

    public ImageProcessor(ImagePreprocessor preprocessor, IImageRejection processingFailedRejection) {
        this.preprocessor = Objects.requireNonNull(preprocessor, "image processor requires a preprocessor");
        this.processingFailedRejection = Objects.requireNonNull(processingFailedRejection,
                "image processor requires a processing-failed rejection");
    }

    public ByteBuffer prepare(String imagePath, IClassificationResult visitor) {
        ByteBuffer preparedBuffer = preprocessor.prepare(imagePath);
        if (preparedBuffer == null) {
            processingFailedRejection.encode(visitor);
        }
        return preparedBuffer;
    }
}
