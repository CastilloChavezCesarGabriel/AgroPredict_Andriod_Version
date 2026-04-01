package com.agropredict.infrastructure.persistence.repository;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import java.util.ArrayList;
import java.util.List;

public abstract class SqliteRepository<T> {
    protected final Database database;
    private final String tableName;

    protected SqliteRepository(Database database, String tableName) {
        this.database = database;
        this.tableName = tableName;
    }

    protected abstract void persist(T entity, SqliteRow row);

    protected SqliteRow write(T entity) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        persist(entity, row);
        return row;
    }

    public void store(T entity) {
        write(entity).flush(tableName);
    }

    protected T restore(Cursor cursor) {
        throw new UnsupportedOperationException();
    }

    protected void deactivate(String identifier) {
        SQLiteDatabase writable = this.database.getWritableDatabase();
        writable.execSQL("UPDATE " + tableName + " SET is_active = 0 WHERE id = ?",
                new Object[]{identifier});
    }

    @SuppressWarnings("SameParameterValue")
    protected T locate(String query, String parameter) {
        SQLiteDatabase readable = this.database.getReadableDatabase();
        Cursor cursor = readable.rawQuery(query, new String[]{parameter});
        T entity = cursor.moveToFirst() ? restore(cursor) : null;
        cursor.close();
        return entity;
    }

    @SuppressWarnings("SameParameterValue")
    protected List<T> fetch(String query, String[] parameters) {
        SQLiteDatabase readable = this.database.getReadableDatabase();
        Cursor cursor = readable.rawQuery(query, parameters);
        List<T> entities = read(cursor);
        cursor.close();
        return entities;
    }

    private List<T> read(Cursor cursor) {
        List<T> entities = new ArrayList<>();
        while (cursor.moveToNext()) {
            entities.add(restore(cursor));
        }
        return entities;
    }
}