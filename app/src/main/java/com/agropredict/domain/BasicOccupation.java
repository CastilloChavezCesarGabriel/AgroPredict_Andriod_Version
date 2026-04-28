package com.agropredict.domain;

public final class BasicOccupation extends Occupation {
    public BasicOccupation(String value) {
        super(value);
    }

    @Override
    public void accept(IOccupationVisitor visitor) {
        visitor.visit(value, false);
    }
}