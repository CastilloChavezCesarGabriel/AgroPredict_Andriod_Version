package com.agropredict.domain.authentication.attempt;

import com.agropredict.domain.authentication.gate.LoginGate;

public final class InitialAttempt implements ILoginAttempt {
    @Override
    public void evaluate(long currentTime, LoginGate gate) {
        gate.allow();
    }

    @Override
    public ILoginAttempt fail(long currentTime) {
        return new TrackedAttempt(1);
    }

    @Override
    public ILoginAttempt succeed() {
        return this;
    }
}
