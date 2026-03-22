package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class UserTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS user ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "occupation_id TEXT REFERENCES occupation(id) ON DELETE SET NULL, "
            + "full_name TEXT NOT NULL, "
            + "username TEXT NOT NULL UNIQUE, "
            + "email TEXT NOT NULL UNIQUE, "
            + "password_hash TEXT NOT NULL, "
            + "phone_number TEXT, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP, "
            + "updated_at TEXT DEFAULT CURRENT_TIMESTAMP, "
            + "is_active INTEGER DEFAULT 1 CHECK (is_active IN (0,1)))");
        database.execSQL(
            "CREATE TRIGGER IF NOT EXISTS user_updated "
            + "AFTER UPDATE ON user FOR EACH ROW BEGIN "
            + "UPDATE user SET updated_at = CURRENT_TIMESTAMP WHERE id = NEW.id; END");
    }
}