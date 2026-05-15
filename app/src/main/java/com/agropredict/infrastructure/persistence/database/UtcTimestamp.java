package com.agropredict.infrastructure.persistence.database;

import com.agropredict.application.service.IClock;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;

public final class UtcTimestamp {
    private static final String FORMAT = "yyyy-MM-dd HH:mm:ss";
    private final IClock clock;

    public UtcTimestamp(IClock clock) {
        this.clock = Objects.requireNonNull(clock, "utc timestamp requires a clock");
    }

    public String serialize() {
        SimpleDateFormat formatter = new SimpleDateFormat(FORMAT, Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(new Date(clock.read()));
    }
}
