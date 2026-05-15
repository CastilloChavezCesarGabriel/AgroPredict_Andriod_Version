package com.agropredict.infrastructure.image_classification;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.OpenableColumns;
import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;
import java.util.Objects;

public final class SizeChecker implements IImageChecker {
    private static final long MIN_BYTES = 10L * 1024;
    private static final long MAX_BYTES = 10L * 1024 * 1024;
    private final IImageRejection tooSmallRejection;
    private final IImageRejection tooLargeRejection;

    public SizeChecker(IImageRejection tooSmallRejection, IImageRejection tooLargeRejection) {
        this.tooSmallRejection = Objects.requireNonNull(tooSmallRejection,
                "size checker requires a too-small rejection");
        this.tooLargeRejection = Objects.requireNonNull(tooLargeRejection,
                "size checker requires a too-large rejection");
    }

    @Override
    public IImageRejection inspect(Uri uri, Context context) {
        long bytes = sizeOf(uri, context);
        if (bytes < MIN_BYTES) return tooSmallRejection;
        if (bytes > MAX_BYTES) return tooLargeRejection;
        return null;
    }

    private long sizeOf(Uri uri, Context context) {
        try (Cursor cursor = context.getContentResolver()
                .query(uri, new String[]{OpenableColumns.SIZE}, null, null, null)) {
            if (cursor == null || !cursor.moveToFirst()) return 0L;
            int index = cursor.getColumnIndex(OpenableColumns.SIZE);
            return index < 0 ? 0L : cursor.getLong(index);
        }
    }
}
