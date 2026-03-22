package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class SupportTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS sync_pending ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "table_name TEXT NOT NULL, "
            + "operation TEXT NOT NULL CHECK (operation IN ('INSERT','UPDATE','DELETE')), "
            + "json_data TEXT NOT NULL, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_sync_user ON sync_pending(user_id)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS log_entry ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "log_action TEXT NOT NULL, "
            + "description TEXT, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_log_user ON log_entry(user_id)");
    }
}