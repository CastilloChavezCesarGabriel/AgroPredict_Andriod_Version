package com.agropredict.factory;

import com.agropredict.application.factory.failure.IFullNameFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;

public final class StubFullNameFailureFactory implements IFullNameFailureFactory {
    @Override
    public IValidationFailure createEmptyFullName() {
        return callback -> callback.receive("Full name is required");
    }

    @Override
    public IValidationFailure createInvalidFullNameLength() {
        return callback -> callback.receive("Full name length is invalid");
    }

    @Override
    public IValidationFailure createInvalidFullNameCharacter() {
        return callback -> callback.receive("Full name contains invalid characters");
    }
}
