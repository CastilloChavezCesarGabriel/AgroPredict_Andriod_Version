package com.agropredict.factory;

import com.agropredict.application.factory.failure.IEmailFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;

public final class StubEmailFailureFactory implements IEmailFailureFactory {
    @Override
    public IValidationFailure createInvalidEmail() {
        return callback -> callback.receive("Invalid email");
    }
}
