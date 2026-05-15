package com.agropredict.infrastructure.persistence.database;

import com.agropredict.application.service.IClock;
import java.lang.System;

public final class SystemClock implements IClock {
    @Override
    public long read() {
        return System.currentTimeMillis();
    }
}
