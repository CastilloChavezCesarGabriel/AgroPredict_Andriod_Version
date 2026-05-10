package com.agropredict.domain.user;

public final class StandardOccupationKind implements IOccupationKind {
    @Override
    public Occupation classify(String value) {
        return new StandardOccupation(value);
    }
}
