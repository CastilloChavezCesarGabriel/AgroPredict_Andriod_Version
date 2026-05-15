package com.agropredict.domain.input_validation;

import com.agropredict.domain.input_validation.gate.IValidationGate;

public interface ITextValidator {
    void check(String text, IValidationGate gate);
}