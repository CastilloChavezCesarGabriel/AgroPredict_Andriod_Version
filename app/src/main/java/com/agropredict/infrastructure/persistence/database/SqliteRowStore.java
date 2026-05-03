package com.agropredict.infrastructure.persistence.database;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.ArrayList;
import java.util.List;

public final class SqliteRowStore {
    private final Database database;

    public SqliteRowStore(Database database) {
        this.database = database;
    }

    public <T> T locate(String query, String parameter, IRowMapper<T> mapper) {
        SQLiteDatabase readable = database.getReadableDatabase();
        Cursor cursor = readable.rawQuery(query, new String[]{parameter});
        T entity = cursor.moveToFirst() ? mapper.map(cursor) : null;
        cursor.close();
        return entity;
    }

    public <T> List<T> fetch(String query, String[] parameters, IRowMapper<T> mapper) {
        SQLiteDatabase readable = database.getReadableDatabase();
        Cursor cursor = readable.rawQuery(query, parameters);
        List<T> entities = new ArrayList<>();
        while (cursor.moveToNext()) {
            entities.add(mapper.map(cursor));
        }
        cursor.close();
        return entities;
    }

    public void deactivate(String tableName, String identifier) {
        database.getWritableDatabase().execSQL(
                "UPDATE " + tableName + " SET is_active = 0 WHERE id = ?",
                new Object[]{identifier});
    }
}
