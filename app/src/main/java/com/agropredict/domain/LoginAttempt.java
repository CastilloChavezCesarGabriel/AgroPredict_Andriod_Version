package com.agropredict.domain;

public final class LoginAttempt {
    private static final int MAXIMUM_ATTEMPTS = 5;
    private static final long BLOCK_DURATION_MILLIS = 5 * 60 * 1000;
    private final int failedAttempts;
    private final long blockedUntil;

    public LoginAttempt() {
        this(0, 0);
    }

    private LoginAttempt(int failedAttempts, long blockedUntil) {
        this.failedAttempts = failedAttempts;
        this.blockedUntil = blockedUntil;
    }

    public boolean isBlocked(long currentTime) {
        return blockedUntil > 0 && currentTime <= blockedUntil;
    }

    public boolean isExhausted() {
        return failedAttempts >= MAXIMUM_ATTEMPTS;
    }

    public LoginAttempt fail(long currentTime) {
        if (!isBlocked(currentTime) && blockedUntil > 0) {
            return new LoginAttempt(0, 0);
        }
        if (isBlocked(currentTime)) return this;
        int updated = failedAttempts + 1;
        if (updated >= MAXIMUM_ATTEMPTS) {
            return new LoginAttempt(updated, currentTime + BLOCK_DURATION_MILLIS);
        }
        return new LoginAttempt(updated, blockedUntil);
    }

    public LoginAttempt succeed() {
        return new LoginAttempt();
    }
}