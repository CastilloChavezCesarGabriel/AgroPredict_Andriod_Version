package com.agropredict.factory;

import com.agropredict.application.factory.failure.IPasswordFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;

public final class StubPasswordFailureFactory implements IPasswordFailureFactory {
    @Override
    public IValidationFailure createUndersizedPassword() {
        return callback -> callback.receive("Password is too short");
    }

    @Override
    public IValidationFailure createRepetitivePassphrase() {
        return callback -> callback.receive("Passphrase has too few unique characters");
    }

    @Override
    public IValidationFailure createWeakPassword() {
        return callback -> callback.receive("Password does not meet complexity requirements");
    }
}
