package com.agropredict.infrastructure.persistence.database;

import java.util.Objects;

public final class SqliteRowFactory {
    private final Database database;
    private final UtcTimestamp timestamp;

    public SqliteRowFactory(Database database, UtcTimestamp timestamp) {
        this.database = Objects.requireNonNull(database, "sqlite row factory requires a database");
        this.timestamp = Objects.requireNonNull(timestamp, "sqlite row factory requires a timestamp");
    }

    public SqliteRow open() {
        return new SqliteRow(database.getWritableDatabase(), timestamp);
    }
}
