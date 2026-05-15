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
            + "id TEXT PRIMARY KEY, "
            + "question_key TEXT NOT NULL UNIQUE, "
            + "text TEXT NOT NULL, "
            + "position INTEGER NOT NULL, "
            + "answer_type TEXT NOT NULL)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS ai_option ("
            + "id TEXT PRIMARY KEY, "
            + "question_id TEXT NOT NULL REFERENCES ai_question(id) ON DELETE CASCADE, "
            + "option_text TEXT NOT NULL, "
            + "option_value TEXT)");
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS ai_user_response ("
            + "id TEXT PRIMARY KEY, "
            + "diagnostic_id TEXT NOT NULL REFERENCES diagnostic(id) ON DELETE CASCADE, "
            + "question_id TEXT NOT NULL REFERENCES ai_question(id) ON DELETE CASCADE, "
            + "option_id TEXT REFERENCES ai_option(id) ON DELETE SET NULL, "
            + "text_value TEXT, "
            + "created_at TEXT NOT NULL)");
    }

    @Override
    public void drop(SQLiteDatabase database) {
        database.execSQL("DROP TABLE IF EXISTS ai_user_response");
        database.execSQL("DROP TABLE IF EXISTS ai_option");
        database.execSQL("DROP TABLE IF EXISTS ai_question");
    }
}
