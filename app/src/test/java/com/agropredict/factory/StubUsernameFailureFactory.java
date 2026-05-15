package com.agropredict.factory;

import com.agropredict.application.factory.failure.IUsernameFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;

public final class StubUsernameFailureFactory implements IUsernameFailureFactory {
    @Override
    public IValidationFailure createEmptyUsername() {
        return callback -> callback.receive("Username is required");
    }

    @Override
    public IValidationFailure createInvalidUsernameLength() {
        return callback -> callback.receive("Username length is invalid");
    }

    @Override
    public IValidationFailure createInvalidUsernameCharacter() {
        return callback -> callback.receive("Username contains invalid characters");
    }

    @Override
    public IValidationFailure createLetterlessUsername() {
        return callback -> callback.receive("Username must contain at least one letter");
    }
}
