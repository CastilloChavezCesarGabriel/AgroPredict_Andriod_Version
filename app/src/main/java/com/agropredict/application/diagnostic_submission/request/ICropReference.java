package com.agropredict.application.diagnostic_submission.request;

import com.agropredict.application.crop_management.usecase.RegisterCropUseCase;
import com.agropredict.domain.identifier.IIdentifierConsumer;

public interface ICropReference {
    void establish(RegisterCropUseCase useCase, IIdentifierConsumer consumer);
}
