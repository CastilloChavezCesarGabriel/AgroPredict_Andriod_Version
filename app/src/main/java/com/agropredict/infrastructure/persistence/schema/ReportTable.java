package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class ReportTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS report ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "diagnostic_id TEXT REFERENCES diagnostic(id) ON DELETE SET NULL, "
            + "crop_id TEXT REFERENCES crop(id) ON DELETE SET NULL, "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "format TEXT NOT NULL CHECK (format IN ('pdf','csv')), "
            + "file_path TEXT, "
            + "generated_at TEXT DEFAULT CURRENT_TIMESTAMP)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_report_user ON report(user_id)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS report_sharing ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "report_id TEXT NOT NULL REFERENCES report(id) ON DELETE CASCADE, "
            + "qr_code TEXT NOT NULL UNIQUE, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP, "
            + "expiration TEXT)");
    }
}