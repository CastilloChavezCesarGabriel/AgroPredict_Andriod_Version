package com.agropredict.application.factory.failure;

import com.agropredict.domain.input_validation.failure.IValidationFailure;

public interface IEmailFailureFactory {
    IValidationFailure createInvalidEmail();
}