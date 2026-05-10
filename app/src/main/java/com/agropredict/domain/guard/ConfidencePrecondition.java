package com.agropredict.domain.guard;

public final class ConfidencePrecondition {
    public static double validate(double value) {
        if (Double.isNaN(value) || value < 0.0 || value > 1.0) {
            throw new IllegalArgumentException("confidence must be in [0, 1]");
        }
        return value;
    }
}