package com.agropredict.domain.input_validation.rule;

public final class Range {
    private final int minimum;
    private final int maximum;

    public Range(int minimum, int maximum) {
        validate(minimum, maximum);
        this.minimum = minimum;
        this.maximum = maximum;
    }

    public boolean contains(int value) {
        return value >= minimum && value <= maximum;
    }

    private static void validate(int minimum, int maximum) {
        if (minimum > maximum) {
            throw new IllegalArgumentException("range requires minimum <= maximum");
        }
    }
}
