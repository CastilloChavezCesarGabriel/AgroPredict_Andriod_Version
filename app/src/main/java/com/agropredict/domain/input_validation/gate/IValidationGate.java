package com.agropredict.domain.input_validation.gate;

import com.agropredict.domain.input_validation.failure.IValidationFailure;

public interface IValidationGate {
    void pass();
    void fail(IValidationFailure failure);
}
