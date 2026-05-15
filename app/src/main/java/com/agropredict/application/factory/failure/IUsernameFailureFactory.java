package com.agropredict.application.factory.failure;

import com.agropredict.domain.input_validation.failure.IValidationFailure;

public interface IUsernameFailureFactory {
    IValidationFailure createEmptyUsername();
    IValidationFailure createInvalidUsernameLength();
    IValidationFailure createInvalidUsernameCharacter();
    IValidationFailure createLetterlessUsername();
}
