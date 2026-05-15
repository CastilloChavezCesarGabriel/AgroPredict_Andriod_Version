package com.agropredict.domain.authentication.attempt;

import com.agropredict.domain.authentication.gate.LoginGate;

public final class TrackedAttempt implements ILoginAttempt {
    private static final int MAXIMUM_ATTEMPTS = 5;
    private static final long BLOCK_DURATION_MILLIS = 5 * 60 * 1000;
    private final int failedAttempts;

    public TrackedAttempt(int failedAttempts) {
        if (failedAttempts < 1 || failedAttempts >= MAXIMUM_ATTEMPTS) {
            throw new IllegalArgumentException("tracked attempt requires 1.." + (MAXIMUM_ATTEMPTS - 1) + " failures");
        }
        this.failedAttempts = failedAttempts;
    }

    @Override
    public void evaluate(long currentTime, LoginGate gate) {
        gate.allow();
    }

    @Override
    public ILoginAttempt fail(long currentTime) {
        int updated = failedAttempts + 1;
        if (updated >= MAXIMUM_ATTEMPTS) {
            return new BlockedAttempt(currentTime + BLOCK_DURATION_MILLIS);
        }
        return new TrackedAttempt(updated);
    }

    @Override
    public ILoginAttempt succeed() {
        return new InitialAttempt();
    }
}
