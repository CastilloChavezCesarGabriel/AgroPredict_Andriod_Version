package com.agropredict.domain.user.occupation;

import com.agropredict.domain.user.Occupation;

public final class StandardOccupation extends Occupation {
    public StandardOccupation(String value) {
        super(value);
    }

    @Override
    public void accept(IOccupationListener listener) {
        listener.onLimit(value);
    }
}