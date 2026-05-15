package com.agropredict.application.backup;

public final class BackupPolicy {
    private final long intervalMillis;

    public BackupPolicy(long intervalMillis) {
        this.intervalMillis = validate(intervalMillis);
    }

    public boolean permits(long elapsedMillis) {
        return elapsedMillis >= intervalMillis;
    }

    private static long validate(long value) {
        if (value < 0) {
            throw new IllegalArgumentException("backup policy interval must be non-negative");
        }
        return value;
    }
}
