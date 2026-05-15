package com.agropredict.factory;

import com.agropredict.application.factory.failure.IPhoneNumberFailureFactory;
import com.agropredict.domain.input_validation.failure.IValidationFailure;

public final class StubPhoneNumberFailureFactory implements IPhoneNumberFailureFactory {
    @Override
    public IValidationFailure createInvalidPhoneNumberLength() {
        return callback -> callback.receive("Phone number length is invalid");
    }

    @Override
    public IValidationFailure createInvalidPhoneNumberCharacter() {
        return callback -> callback.receive("Phone number contains invalid characters");
    }
}
