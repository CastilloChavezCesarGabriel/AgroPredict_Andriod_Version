package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IdentifierFactory;

public final class QuestionSeed {
    private final String key;
    private final String text;
    private final int position;

    public QuestionSeed(String key, String text, int position) {
        this.key = ArgumentPrecondition.validate(key, "question seed key");
        this.text = ArgumentPrecondition.validate(text, "question seed text");
        this.position = position;
    }

    public void load(SQLiteDatabase database) {
        database.execSQL(
                "INSERT OR IGNORE INTO ai_question (id, question_key, text, position, answer_type) "
                        + "VALUES (?, ?, ?, ?, 'single')",
                new Object[]{IdentifierFactory.generate("question"), key, text, position});
    }
}
