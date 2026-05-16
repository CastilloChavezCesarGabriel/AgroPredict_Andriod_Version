package com.agropredict.infrastructure.image_classification.validation;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public final class DimensionChecker implements IImageChecker {
    private static final int MIN_PIXELS = 100;
    private static final int MAX_PIXELS = 8000;
    private final IImageRejection tooSmallRejection;
    private final IImageRejection tooLargeRejection;

    public DimensionChecker(IImageRejection tooSmallRejection, IImageRejection tooLargeRejection) {
        this.tooSmallRejection = Objects.requireNonNull(tooSmallRejection,
                "dimension checker requires a too-small rejection");
        this.tooLargeRejection = Objects.requireNonNull(tooLargeRejection,
                "dimension checker requires a too-large rejection");
    }

    @Override
    public IImageRejection inspect(Uri uri, Context context) {
        BitmapFactory.Options bounds = measure(uri, context);
        if (bounds.outWidth < MIN_PIXELS || bounds.outHeight < MIN_PIXELS) {
            return tooSmallRejection;
        }
        if (bounds.outWidth > MAX_PIXELS || bounds.outHeight > MAX_PIXELS) {
            return tooLargeRejection;
        }
        return null;
    }

    private BitmapFactory.Options measure(Uri uri, Context context) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        try (InputStream stream = context.getContentResolver().openInputStream(uri)) {
            BitmapFactory.decodeStream(stream, null, options);
        } catch (IOException ignored) {
        }
        return options;
    }
}
