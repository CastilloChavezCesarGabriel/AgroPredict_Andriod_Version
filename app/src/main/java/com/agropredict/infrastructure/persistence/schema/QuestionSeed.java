package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.domain.Identifier;

public final class QuestionSeed {
    private final String key;
    private final String text;

    public QuestionSeed(String key, String text) {
        this.key = key;
        this.text = text;
    }

    public void load(SQLiteDatabase database) {
        database.execSQL(
            "INSERT OR IGNORE INTO ai_question (id, question_key, text, position, answer_type) "
            + "VALUES (?, ?, ?, (SELECT COALESCE(MAX(position), 0) + 1 FROM ai_question), 'single')",
            new Object[]{Identifier.generate("question"), key, text});
    }
}
