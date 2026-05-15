package com.agropredict.application.backup;

import com.agropredict.domain.guard.MagnitudePrecondition;

public final class BackupPolicy {
    private final long intervalMillis;

    public BackupPolicy(long intervalMillis) {
        this.intervalMillis = MagnitudePrecondition.validate(intervalMillis, "backup policy interval");
    }

    public boolean permits(long elapsedMillis) {
        return elapsedMillis >= intervalMillis;
    }
}
