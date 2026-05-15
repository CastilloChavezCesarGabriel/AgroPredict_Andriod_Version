package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class SupportTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        define(database);
        index(database);
    }

    private void define(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS sync_pending ("
            + "id TEXT PRIMARY KEY, "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "table_name TEXT NOT NULL, "
            + "operation TEXT NOT NULL, "
            + "json_data TEXT NOT NULL, "
            + "created_at TEXT NOT NULL)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS log_entry ("
            + "id TEXT PRIMARY KEY, "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "log_action TEXT NOT NULL, "
            + "description TEXT, "
            + "created_at TEXT NOT NULL)");
    }

    private void index(SQLiteDatabase database) {
        database.execSQL("CREATE INDEX IF NOT EXISTS index_sync_user ON sync_pending(user_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_log_user ON log_entry(user_id)");
    }
}
