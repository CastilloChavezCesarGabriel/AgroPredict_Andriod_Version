package com.agropredict.infrastructure.image_classification;

import android.content.Context;
import android.net.Uri;
import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;

public interface IImageChecker {
    IImageRejection inspect(Uri uri, Context context);
}
