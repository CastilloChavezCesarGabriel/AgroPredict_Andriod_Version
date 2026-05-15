package com.agropredict.domain.input_validation;

import com.agropredict.domain.input_validation.failure.IValidationFailure;
import com.agropredict.domain.input_validation.gate.IValidationGate;
import java.util.ArrayList;
import java.util.List;

public final class CapturingValidationGate implements IValidationGate {
    private final List<String> outcomes = new ArrayList<>();

    @Override
    public void pass() {
        outcomes.add("pass");
    }

    @Override
    public void fail(IValidationFailure failure) {
        outcomes.add("fail");
    }

    public boolean hasPassed() {
        return outcomes.contains("pass");
    }

    public boolean hasFailed() {
        return outcomes.contains("fail");
    }
}