package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class QuestionnaireTable implements ITable {
    @Override
    public void create(SQLiteDatabase database) {
        define(database);
    }

    private void define(SQLiteDatabase database) {
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