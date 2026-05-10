package com.agropredict.domain.input_validation;

import java.util.Objects;

public final class LengthRangeRule extends TextRule {
    private final Range range;

    public LengthRangeRule(Range range, String reason) {
        super(reason);
        this.range = Objects.requireNonNull(range, "length range rule requires a range");
    }

    @Override
    protected boolean accepts(String text) {
        int length = text == null ? 0 : text.length();
        return range.contains(length);
    }
}