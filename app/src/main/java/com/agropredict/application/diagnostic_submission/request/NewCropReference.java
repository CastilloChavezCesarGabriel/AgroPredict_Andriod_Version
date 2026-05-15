package com.agropredict.application.diagnostic_submission.request;

import com.agropredict.application.crop_management.usecase.RegisterCropUseCase;
import com.agropredict.domain.crop.Crop;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IIdentifierConsumer;
import java.util.Objects;

public final class NewCropReference implements ICropReference {
    private final String cropIdentifier;
    private final Crop crop;

    public NewCropReference(String cropIdentifier, Crop crop) {
        this.cropIdentifier = ArgumentPrecondition.validate(cropIdentifier, "new crop reference identifier");
        this.crop = Objects.requireNonNull(crop, "new crop reference requires a crop");
    }

    @Override
    public void establish(RegisterCropUseCase useCase, IIdentifierConsumer consumer) {
        useCase.register(crop);
        consumer.identify(cropIdentifier);
    }
}
