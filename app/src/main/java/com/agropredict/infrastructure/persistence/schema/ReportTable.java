package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class ReportTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        define(database);
        index(database);
    }

    private void define(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS report ("
            + "id TEXT PRIMARY KEY, "
            + "diagnostic_id TEXT REFERENCES diagnostic(id) ON DELETE SET NULL, "
            + "crop_id TEXT REFERENCES crop(id) ON DELETE SET NULL, "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "format TEXT NOT NULL CHECK (format IN ('pdf','csv')), "
            + "file_path TEXT, "
            + "is_active INTEGER NOT NULL CHECK (is_active IN (0,1)), "
            + "generated_at TEXT NOT NULL)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS report_diagnostic ("
            + "report_id TEXT NOT NULL REFERENCES report(id) ON DELETE CASCADE, "
            + "diagnostic_id TEXT NOT NULL REFERENCES diagnostic(id) ON DELETE CASCADE, "
            + "position INTEGER, "
            + "observations TEXT, "
            + "PRIMARY KEY (report_id, diagnostic_id))");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS report_sharing ("
            + "id TEXT PRIMARY KEY, "
            + "report_id TEXT NOT NULL REFERENCES report(id) ON DELETE CASCADE, "
            + "qr_code TEXT NOT NULL UNIQUE, "
            + "created_at TEXT NOT NULL, "
            + "expiration TEXT)");
    }

    private void index(SQLiteDatabase database) {
        database.execSQL("CREATE INDEX IF NOT EXISTS index_report_user ON report(user_id)");
    }
}
