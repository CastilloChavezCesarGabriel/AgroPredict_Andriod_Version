package com.agropredict.presentation.user_interface.screen;

import com.agropredict.application.diagnostic_submission.rejection.IImageRejection;
import com.agropredict.application.service.IImageCompressor;
import com.agropredict.application.service.IImageValidator;
import java.util.Objects;

public final class ImageWorkbench {
    private final IImageCompressor compressor;
    private final IImageValidator validator;

    public ImageWorkbench(IImageCompressor compressor, IImageValidator validator) {
        this.compressor = Objects.requireNonNull(compressor, "image workbench requires a compressor");
        this.validator = Objects.requireNonNull(validator, "image workbench requires a validator");
    }

    public IImageRejection validate(String imageUri) {
        return validator.validate(imageUri);
    }

    public String compress(String imageUri) {
        return compressor.compress(imageUri);
    }
}
