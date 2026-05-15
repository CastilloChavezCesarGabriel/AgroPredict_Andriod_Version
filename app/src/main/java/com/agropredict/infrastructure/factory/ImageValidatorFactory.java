package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.factory.IImageRejectionFactory;
import com.agropredict.infrastructure.image_classification.DimensionChecker;
import com.agropredict.infrastructure.image_classification.FormatChecker;
import com.agropredict.infrastructure.image_classification.IImageChecker;
import com.agropredict.infrastructure.image_classification.ImageValidator;
import com.agropredict.infrastructure.image_classification.SizeChecker;
import java.util.List;
import java.util.Objects;

public final class ImageValidatorFactory {
    private static final List<String> ALLOWED_MIMES = List.of("image/jpeg", "image/png");
    private final Context context;
    private final IImageRejectionFactory rejectionFactory;

    public ImageValidatorFactory(Context context, IImageRejectionFactory rejectionFactory) {
        this.context = Objects.requireNonNull(context,
                "image validator factory requires a context");
        this.rejectionFactory = Objects.requireNonNull(rejectionFactory,
                "image validator factory requires a rejection factory");
    }

    public ImageValidator create() {
        List<IImageChecker> checkers = List.of(
                new FormatChecker(ALLOWED_MIMES, rejectionFactory.createFormatUnsupported()),
                new SizeChecker(rejectionFactory.createFileSizeTooSmall(), rejectionFactory.createFileSizeTooLarge()),
                new DimensionChecker(rejectionFactory.createDimensionTooSmall(), rejectionFactory.createDimensionTooLarge()));
        return new ImageValidator(context, checkers);
    }
}
