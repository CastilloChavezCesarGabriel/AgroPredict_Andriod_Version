package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.domain.identifier.IdentifierFactory;

public final class OptionSeed {
    private final String questionKey;
    private final String[] options;

    public OptionSeed(String questionKey, String[] options) {
        this.questionKey = questionKey;
        this.options = options;
    }

    public void load(SQLiteDatabase database) {
        for (String option : options) {
            database.execSQL(
                    "INSERT OR IGNORE INTO ai_option (id, question_id, option_text, option_value) "
                            + "VALUES (?, (SELECT id FROM ai_question WHERE question_key = ?), ?, ?)",
                    new Object[]{IdentifierFactory.generate("option"), questionKey, option, option});
        }
    }
}