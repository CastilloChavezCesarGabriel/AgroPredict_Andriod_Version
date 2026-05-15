package com.agropredict.infrastructure.persistence.database;

import com.agropredict.domain.guard.ArgumentPrecondition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public final class UtcTimestamp {
    private final String pattern;

    public UtcTimestamp(String pattern) {
        this.pattern = ArgumentPrecondition.validate(pattern, "utc timestamp pattern");
    }

    public String serialize(long millis) {
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        return formatter.format(new Date(millis));
    }
}
