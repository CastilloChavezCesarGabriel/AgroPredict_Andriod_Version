package com.agropredict.application.diagnostic_submission.request;

import com.agropredict.application.crop_management.usecase.RegisterCropUseCase;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IIdentifierConsumer;

public final class ExistingCropReference implements ICropReference {
    private final String cropIdentifier;

    public ExistingCropReference(String cropIdentifier) {
        this.cropIdentifier = ArgumentPrecondition.validate(cropIdentifier, "existing crop reference identifier");
    }

    @Override
    public void establish(RegisterCropUseCase useCase, IIdentifierConsumer consumer) {
        consumer.identify(cropIdentifier);
    }
}
