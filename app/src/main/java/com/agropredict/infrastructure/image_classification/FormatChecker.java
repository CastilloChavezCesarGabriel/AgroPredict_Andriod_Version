package com.agropredict.infrastructure.image_classification;

import android.content.Context;
import android.net.Uri;
import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;
import java.util.List;
import java.util.Objects;

public final class FormatChecker implements IImageChecker {
    private final List<String> allowedMimes;
    private final IImageRejection unsupportedRejection;

    public FormatChecker(List<String> allowedMimes, IImageRejection unsupportedRejection) {
        this.allowedMimes = List.copyOf(Objects.requireNonNull(allowedMimes,
                "format checker requires allowed mimes"));
        this.unsupportedRejection = Objects.requireNonNull(unsupportedRejection,
                "format checker requires an unsupported rejection");
    }

    @Override
    public IImageRejection inspect(Uri uri, Context context) {
        String mime = context.getContentResolver().getType(uri);
        if (mime == null || !allowedMimes.contains(mime)) return unsupportedRejection;
        return null;
    }
}
