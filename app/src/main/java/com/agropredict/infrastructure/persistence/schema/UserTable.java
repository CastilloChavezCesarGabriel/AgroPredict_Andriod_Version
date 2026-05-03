package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class UserTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS user ("
            + "id TEXT PRIMARY KEY, "
            + "occupation_id TEXT REFERENCES occupation(id) ON DELETE SET NULL, "
            + "full_name TEXT NOT NULL, "
            + "username TEXT NOT NULL UNIQUE, "
            + "email TEXT NOT NULL UNIQUE, "
            + "password_hash TEXT NOT NULL, "
            + "phone_number TEXT, "
            + "created_at TEXT NOT NULL, "
            + "updated_at TEXT NOT NULL, "
            + "is_active INTEGER NOT NULL CHECK (is_active IN (0,1)))");
    }
}
