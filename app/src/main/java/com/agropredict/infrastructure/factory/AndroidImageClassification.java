package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.factory.IImageRejectionFactory;
import com.agropredict.application.factory.IImageClassificationFactory;
import com.agropredict.application.service.IImageClassifier;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.application.service.IImageValidator;
import com.agropredict.infrastructure.image_classification.BitmapCompressor;
import com.agropredict.infrastructure.image_classification.TFLiteClassifier;
import java.util.Objects;

public final class AndroidImageClassification implements IImageClassificationFactory {
    private final Context context;
    private final IImageRejectionFactory rejectionFactory;

    public AndroidImageClassification(Context context, IImageRejectionFactory rejectionFactory) {
        this.context = Objects.requireNonNull(context, "android image classification requires a context");
        this.rejectionFactory = Objects.requireNonNull(rejectionFactory, "android image classification requires a rejection factory");
    }

    @Override
    public IImageClassifier createImageClassifier() {
        return new TFLiteClassifier(
                new TFLiteModelFactory(context, rejectionFactory).create(),
                new ImageProcessorFactory(rejectionFactory).create());
    }

    @Override
    public IImageCompressor createImageCompressor() {
        return new BitmapCompressor(context);
    }

    @Override
    public IImageValidator createImageValidator() {
        return new ImageValidatorFactory(context, rejectionFactory).create();
    }
}
