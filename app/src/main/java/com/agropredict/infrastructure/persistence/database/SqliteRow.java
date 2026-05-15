package com.agropredict.infrastructure.persistence.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.service.IClock;
import java.util.Objects;

public final class SqliteRow {
    private final ContentValues values;
    private final SQLiteDatabase database;
    private final IClock clock;
    private final UtcTimestamp timestamp;

    public SqliteRow(SQLiteDatabase database, IClock clock) {
        this.values = new ContentValues();
        this.database = Objects.requireNonNull(database, "sqlite row requires a database");
        this.clock = Objects.requireNonNull(clock, "sqlite row requires a clock");
        this.timestamp = new UtcTimestamp("yyyy-MM-dd HH:mm:ss");
    }

    public void record(String column, String value) {
        values.put(column, value);
    }

    public void mark(String column, int value) {
        values.put(column, value);
    }

    public void stamp(String column) {
        values.put(column, timestamp.serialize(clock.read()));
    }

    public void imprint(String column, long millis) {
        values.put(column, timestamp.serialize(millis));
    }

    public void flush(String table) {
        database.insertWithOnConflict(table, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean overwrite(String table, String keyColumn) {
        int rows = database.update(table, values, keyColumn + " = ?",
                new String[]{values.getAsString(keyColumn)});
        return rows > 0;
    }
}
