package com.agropredict.application.backup;

import com.agropredict.application.service.IClock;
import java.util.Objects;

public final class BackupSchedule {
    private final BackupPolicy policy;
    private final IClock clock;

    public BackupSchedule(BackupPolicy policy, IClock clock) {
        this.policy = Objects.requireNonNull(policy, "backup schedule requires a policy");
        this.clock = Objects.requireNonNull(clock, "backup schedule requires a clock");
    }

    public boolean permits(long lastTimeMillis) {
        return policy.permits(clock.read() - lastTimeMillis);
    }
}