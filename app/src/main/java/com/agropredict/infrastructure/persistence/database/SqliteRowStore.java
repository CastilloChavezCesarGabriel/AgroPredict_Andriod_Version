package com.agropredict.infrastructure.persistence.database;

import android.database.Cursor;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

public final class SqliteRowStore {
    private final Database database;

    public SqliteRowStore(Database database) {
        this.database = Objects.requireNonNull(database, "sqlite row store requires a database");
    }

    public <T> T locate(String sql, String parameter, IRowMapper<T> mapper) {
        return read(sql, new String[]{parameter}, cursor ->
                cursor.moveToFirst() ? mapper.map(cursor) : null);
    }

    public <T> List<T> fetch(String sql, String[] parameters, IRowMapper<T> mapper) {
        return read(sql, parameters, cursor -> {
            List<T> entities = new ArrayList<>();
            while (cursor.moveToNext()) {
                entities.add(mapper.map(cursor));
            }
            return entities;
        });
    }

    public void deactivate(String tableName, String identifier) {
        database.getWritableDatabase().execSQL(
                "UPDATE " + tableName + " SET is_active = 0 WHERE id = ?",
                new Object[]{identifier});
    }

    private <T> T read(String sql, String[] parameters, Function<Cursor, T> action) {
        try (Cursor cursor = database.getReadableDatabase().rawQuery(sql, parameters)) {
            return action.apply(cursor);
        }
    }
}
