package com.agropredict.visitor;

import com.agropredict.domain.user.occupation.IOccupationListener;

public final class LimitExpecter implements IOccupationListener {
    @Override
    public void onElevate(String label) {
        throw new AssertionError("expected onLimit but got onElevate: " + label);
    }

    @Override
    public void onLimit(String label) {}
}