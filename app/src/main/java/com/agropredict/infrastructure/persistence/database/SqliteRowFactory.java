package com.agropredict.infrastructure.persistence.database;

import com.agropredict.application.service.IClock;
import java.util.Objects;

public final class SqliteRowFactory {
    private final Database database;
    private final IClock clock;

    public SqliteRowFactory(Database database, IClock clock) {
        this.database = Objects.requireNonNull(database, "sqlite row factory requires a database");
        this.clock = Objects.requireNonNull(clock, "sqlite row factory requires a clock");
    }

    public SqliteRow open() {
        return new SqliteRow(database.getWritableDatabase(), clock);
    }
}
