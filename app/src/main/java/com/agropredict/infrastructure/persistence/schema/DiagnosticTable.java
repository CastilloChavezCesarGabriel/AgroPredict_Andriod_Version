package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class DiagnosticTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        define(database);
        index(database);
    }

    private void define(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS diagnostic ("
            + "id TEXT PRIMARY KEY, "
            + "crop_id TEXT REFERENCES crop(id) ON DELETE SET NULL, "
            + "image_id TEXT REFERENCES image(id) ON DELETE SET NULL, "
            + "user_id TEXT REFERENCES user(id) ON DELETE SET NULL, "
            + "problem_type_id TEXT REFERENCES catalog_problem_type(id) ON DELETE SET NULL, "
            + "predicted_crop TEXT, "
            + "confidence REAL, "
            + "severity TEXT CHECK (severity IN ('low','moderate','high')), "
            + "temperature REAL, "
            + "humidity REAL, "
            + "recommendation_text TEXT, "
            + "short_summary TEXT, "
            + "created_at TEXT NOT NULL)");
    }

    private void index(SQLiteDatabase database) {
        database.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_crop ON diagnostic(crop_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_user ON diagnostic(user_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_created ON diagnostic(created_at)");
    }
}
