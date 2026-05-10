package com.agropredict.domain.authentication;

public final class InitialAttempt implements ILoginAttempt {
    @Override
    public void evaluate(long currentTime, ILoginGate gate) {
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