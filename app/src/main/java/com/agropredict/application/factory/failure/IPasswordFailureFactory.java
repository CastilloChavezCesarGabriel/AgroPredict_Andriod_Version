package com.agropredict.application.factory.failure;

import com.agropredict.domain.input_validation.failure.IValidationFailure;

public interface IPasswordFailureFactory {
    IValidationFailure createUndersizedPassword();
    IValidationFailure createRepetitivePassphrase();
    IValidationFailure createWeakPassword();
}
