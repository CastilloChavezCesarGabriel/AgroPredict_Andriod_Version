package com.agropredict.presentation.viewmodel.prediction_diagnosis;

import android.content.Context;
import com.agropredict.R;
import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;
import com.agropredict.application.diagnostic_submission.rejection.PhraseRejection;
import com.agropredict.application.factory.IImageRejectionFactory;
import java.util.List;
import java.util.Objects;

public final class AndroidImageRejectionFactory implements IImageRejectionFactory {
    private static final List<String> ALLOWED_EXTENSIONS = List.of(".jpg", ".jpeg", ".png");
    private final Context context;

    public AndroidImageRejectionFactory(Context context) {
        this.context = Objects.requireNonNull(context, "android image rejection factory requires a context");
    }

    @Override
    public IImageRejection createFormatUnsupported() {
        return new PhraseRejection(detail -> context.getString(R.string.image_unsupported_format, detail),
                String.join(", ", ALLOWED_EXTENSIONS));
    }

    @Override
    public IImageRejection createDimensionTooSmall() {
        return new PhraseRejection(detail -> context.getString(R.string.image_dim_too_small), "");
    }

    @Override
    public IImageRejection createDimensionTooLarge() {
        return new PhraseRejection(detail -> context.getString(R.string.image_dim_too_large), "");
    }

    @Override
    public IImageRejection createFileSizeTooSmall() {
        return new PhraseRejection(detail -> context.getString(R.string.image_size_too_small), "");
    }

    @Override
    public IImageRejection createFileSizeTooLarge() {
        return new PhraseRejection(detail -> context.getString(R.string.image_size_too_large), "");
    }

    @Override
    public IImageRejection createProcessingFailed() {
        return new PhraseRejection(detail -> context.getString(R.string.image_processing_failed), "");
    }

    @Override
    public IImageRejection createConfidenceLow() {
        return new PhraseRejection(detail -> context.getString(R.string.classification_low_confidence), "");
    }
}