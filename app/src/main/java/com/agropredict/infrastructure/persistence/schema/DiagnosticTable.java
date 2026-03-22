package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class DiagnosticTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS diagnostic ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
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
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_crop ON diagnostic(crop_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_user ON diagnostic(user_id)");
        database.execSQL("CREATE INDEX IF NOT EXISTS index_diagnostic_created ON diagnostic(created_at)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS ai_question ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "question_key TEXT NOT NULL UNIQUE, "
            + "text TEXT NOT NULL, "
            + "position INTEGER DEFAULT 0, "
            + "answer_type TEXT NOT NULL DEFAULT 'single' "
            + "CHECK (answer_type IN ('single','multiple','numeric','date')))");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS ai_option ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "question_id TEXT NOT NULL REFERENCES ai_question(id) ON DELETE CASCADE, "
            + "option_text TEXT NOT NULL, "
            + "option_value TEXT)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS ai_user_response ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "diagnostic_id TEXT NOT NULL REFERENCES diagnostic(id) ON DELETE CASCADE, "
            + "question_id TEXT NOT NULL REFERENCES ai_question(id) ON DELETE CASCADE, "
            + "option_id TEXT REFERENCES ai_option(id) ON DELETE SET NULL, "
            + "text_value TEXT, "
            + "created_at TEXT DEFAULT CURRENT_TIMESTAMP)");
    }
}
