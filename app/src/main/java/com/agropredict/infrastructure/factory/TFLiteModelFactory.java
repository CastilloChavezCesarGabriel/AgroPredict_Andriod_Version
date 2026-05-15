package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.factory.IImageRejectionFactory;
import com.agropredict.infrastructure.image_classification.InterpreterLoader;
import com.agropredict.infrastructure.image_classification.LabelCatalog;
import com.agropredict.infrastructure.image_classification.TFLiteModel;
import java.util.Objects;

public final class TFLiteModelFactory {
    private static final String MODEL_ASSET = "models/cultivo_model.tflite";
    private static final String LABELS_ASSET = "models/classes.json";
    private final Context context;
    private final IImageRejectionFactory rejectionFactory;

    public TFLiteModelFactory(Context context, IImageRejectionFactory rejectionFactory) {
        this.context = Objects.requireNonNull(context, "tflite model factory requires a context");
        this.rejectionFactory = Objects.requireNonNull(rejectionFactory, "tflite model factory requires a rejection factory");
    }

    public TFLiteModel create() {
        return new TFLiteModel(
                new InterpreterLoader(context.getAssets()).load(MODEL_ASSET),
                new LabelCatalog(context.getAssets()).load(LABELS_ASSET),
                rejectionFactory.createConfidenceLow());
    }
}
