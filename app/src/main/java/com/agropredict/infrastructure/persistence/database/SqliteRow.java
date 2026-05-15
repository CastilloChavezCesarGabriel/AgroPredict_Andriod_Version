package com.agropredict.infrastructure.persistence.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.util.Objects;

public final class SqliteRow {
    private final ContentValues values;
    private final SQLiteDatabase database;
    private final UtcTimestamp timestamp;

    public SqliteRow(SQLiteDatabase database, UtcTimestamp timestamp) {
        this.values = new ContentValues();
        this.database = Objects.requireNonNull(database, "sqlite row requires a database");
        this.timestamp = Objects.requireNonNull(timestamp, "sqlite row requires a timestamp");
    }

    public void record(String column, String value) {
        values.put(column, value);
    }

    public void mark(String column, int value) {
        values.put(column, value);
    }

    public void stamp(String column) {
        values.put(column, timestamp.serialize());
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
