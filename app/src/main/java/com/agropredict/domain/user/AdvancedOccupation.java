package com.agropredict.domain.user;

public final class AdvancedOccupation extends Occupation {
    public AdvancedOccupation(String value) {
        super(value);
    }

    @Override
    public void accept(IOccupationListener listener) {
        listener.onElevate(value);
    }
}