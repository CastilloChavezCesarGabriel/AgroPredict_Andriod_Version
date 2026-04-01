package com.agropredict.infrastructure.persistence.database;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

public final class SqliteRow implements IRow {
    private final ContentValues values;
    private final SQLiteDatabase database;

    public SqliteRow(SQLiteDatabase database) {
        this.values = new ContentValues();
        this.database = database;
    }

    @Override
    public void record(String column, String value) {
        values.put(column, value);
    }

    public void flush(String table) {
        database.insertWithOnConflict(table, null, values,
                SQLiteDatabase.CONFLICT_REPLACE);
    }

    public boolean overwrite(String table, String keyColumn) {
        String identifier = values.getAsString(keyColumn);
        int rows = database.update(table, values, keyColumn + " = ?", new String[]{identifier});
        return rows > 0;
    }
}