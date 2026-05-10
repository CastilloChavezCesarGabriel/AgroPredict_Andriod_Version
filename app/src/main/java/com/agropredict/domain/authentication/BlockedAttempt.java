package com.agropredict.domain.authentication;

public final class BlockedAttempt implements ILoginAttempt {
    private final long blockedUntil;

    public BlockedAttempt(long blockedUntil) {
        if (blockedUntil <= 0) {
            throw new IllegalArgumentException("blocked attempt requires a positive deadline");
        }
        this.blockedUntil = blockedUntil;
    }

    @Override
    public void evaluate(long currentTime, ILoginGate gate) {
        if (currentTime <= blockedUntil) {
            gate.block();
        } else {
            gate.exhaust();
        }
    }

    @Override
    public ILoginAttempt fail(long currentTime) {
        if (currentTime > blockedUntil) {
            return new TrackedAttempt(1);
        }
        return this;
    }

    @Override
    public ILoginAttempt succeed() {
        return new InitialAttempt();
    }
}