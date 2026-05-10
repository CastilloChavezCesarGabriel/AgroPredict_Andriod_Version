package com.agropredict.domain.input_validation;

public interface IValidationGate {
    void pass();
    void fail(String reason);
}