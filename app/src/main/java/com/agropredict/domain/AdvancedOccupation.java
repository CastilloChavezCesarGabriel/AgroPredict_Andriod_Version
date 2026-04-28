package com.agropredict.domain;

public final class AdvancedOccupation extends Occupation {
    public AdvancedOccupation(String value) {
        super(value);
    }

    @Override
    public void accept(IOccupationVisitor visitor) {
        visitor.visit(value, true);
    }
}