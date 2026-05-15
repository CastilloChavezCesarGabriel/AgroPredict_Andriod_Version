package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IdentifierFactory;
import java.util.Objects;

public final class OptionSeed {
    private final String questionKey;
    private final String[] options;

    public OptionSeed(String questionKey, String[] options) {
        this.questionKey = ArgumentPrecondition.validate(questionKey, "option seed question key");
        this.options = Objects.requireNonNull(options, "option seed requires options");
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