package com.agropredict.domain.user.occupation;

import com.agropredict.domain.user.Occupation;

public final class StandardOccupationKind implements IOccupationKind {
    @Override
    public Occupation classify(String value) {
        return new StandardOccupation(value);
    }
}
