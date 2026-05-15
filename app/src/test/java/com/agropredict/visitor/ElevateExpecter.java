package com.agropredict.visitor;

import com.agropredict.domain.user.occupation.IOccupationListener;

public final class ElevateExpecter implements IOccupationListener {
    @Override
    public void onElevate(String label) {}

    @Override
    public void onLimit(String label) {
        throw new AssertionError("expected onElevate but got onLimit: " + label);
    }
}
