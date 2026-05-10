package com.agropredict.domain.input_validation;

public interface ITextValidator {
    void check(String text, IValidationGate gate);
}