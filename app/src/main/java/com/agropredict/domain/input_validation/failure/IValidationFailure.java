package com.agropredict.domain.input_validation.failure;

import com.agropredict.domain.input_validation.phrase.IValidationFailureCallback;

public interface IValidationFailure {
    void encode(IValidationFailureCallback callback);
}
