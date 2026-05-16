package com.agropredict.infrastructure.image_classification.validation;

import android.content.Context;
import android.net.Uri;
import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;
import com.agropredict.application.service.IImageValidator;
import java.util.List;
import java.util.Objects;

public final class ImageValidator implements IImageValidator {
    private final Context context;
    private final List<IImageChecker> checkers;

    public ImageValidator(Context context, List<IImageChecker> checkers) {
        this.context = Objects.requireNonNull(context, "image validator requires a context");
        this.checkers = List.copyOf(Objects.requireNonNull(checkers,
                "image validator requires checkers"));
    }

    @Override
    public IImageRejection validate(String uriString) {
        Uri uri = Uri.parse(uriString);
        for (IImageChecker checker : checkers) {
            IImageRejection rejection = checker.inspect(uri, context);
            if (rejection != null) return rejection;
        }
        return null;
    }
}