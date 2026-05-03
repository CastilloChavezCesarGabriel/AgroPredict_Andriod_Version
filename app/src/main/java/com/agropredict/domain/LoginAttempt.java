package com.agropredict.domain;

public final class LoginAttempt {
    private static final int MAXIMUM_ATTEMPTS = 5;
    private static final long BLOCK_DURATION_MILLIS = 5 * 60 * 1000;
    private final int failedAttempts;
    private final long blockedUntil;
    public LoginAttempt(int failedAttempts, long blockedUntil) {
        this.failedAttempts = failedAttempts;
        this.blockedUntil = blockedUntil;
    }

    public void evaluate(long currentTime, ILoginGate gate) {
        if (blocks(currentTime)) {
            gate.block();
            return;
        }
        if (exhausts()) {
            gate.exhaust();
            return;
        }
        gate.allow();
    }

    public LoginAttempt fail(long currentTime) {
        if (expires(currentTime)) return new LoginAttempt(0, 0);
        if (blocks(currentTime)) return this;
        return increment(currentTime);
    }

    public LoginAttempt succeed() {
        return new LoginAttempt(0, 0);
    }

    private boolean blocks(long currentTime) {
        return blockedUntil > 0 && currentTime <= blockedUntil;
    }

    private boolean expires(long currentTime) {
        return blockedUntil > 0 && currentTime > blockedUntil;
    }

    private boolean exhausts() {
        return failedAttempts >= MAXIMUM_ATTEMPTS;
    }

    private LoginAttempt increment(long currentTime) {
        int updated = failedAttempts + 1;
        if (updated >= MAXIMUM_ATTEMPTS) {
            return new LoginAttempt(updated, currentTime + BLOCK_DURATION_MILLIS);
        }
        return new LoginAttempt(updated, blockedUntil);
    }
}
