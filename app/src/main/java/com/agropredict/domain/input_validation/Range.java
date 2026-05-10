package com.agropredict.domain.input_validation;

public final class Range {
    private final int minimum;
    private final int maximum;

    public Range(int minimum, int maximum) {
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public boolean contains(int value) {
        return value >= minimum && value <= maximum;
    }
}