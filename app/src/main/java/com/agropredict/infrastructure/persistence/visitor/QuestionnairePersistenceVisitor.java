package com.agropredict.infrastructure.persistence.visitor;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.IAnswerConsumer;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import java.util.Objects;
import java.util.Set;

public final class QuestionnairePersistenceVisitor implements IAnswerConsumer {
    private final SqliteRowFactory rowFactory;
    private final SQLiteDatabase database;
    private final String diagnosticIdentifier;
    private final Set<String> denormalizedKeys;

    public QuestionnairePersistenceVisitor(SqliteRowFactory rowFactory, SQLiteDatabase database, String diagnosticIdentifier) {
        this.rowFactory = Objects.requireNonNull(rowFactory, "questionnaire persistence visitor requires a row factory");
        this.database = Objects.requireNonNull(database, "questionnaire persistence visitor requires a database");
        this.diagnosticIdentifier = Objects.requireNonNull(diagnosticIdentifier, "questionnaire persistence visitor requires a diagnostic identifier");
        this.denormalizedKeys = Set.of("temperature", "humidity");
    }

    @Override
    public void record(String key, String value) {
        String questionId = resolve(key);
        insert(questionId, key, value);
        propagate(key, value);
    }

    private String resolve(String key) {
        String questionId = locate("ai_question", "question_key", key);
        return questionId != null ? questionId : key;
    }

    private void insert(String questionId, String key, String value) {
        SqliteRow row = rowFactory.open();
        row.record("id", IdentifierFactory.generate(key));
        row.record("diagnostic_id", diagnosticIdentifier);
        row.record("question_id", questionId);
        row.record("option_id", locate("ai_option", "option_text", value));
        row.record("text_value", value);
        row.stamp("created_at");
        row.flush("ai_user_response");
    }

    private void propagate(String key, String value) {
        if (!denormalizedKeys.contains(key)) return;
        database.execSQL("UPDATE diagnostic SET " + key + " = ? WHERE id = ?",
                new Object[]{value, diagnosticIdentifier});
    }

    private String locate(String table, String column, String value) {
        if (value == null) return null;
        Cursor cursor = database.rawQuery(
                "SELECT id FROM " + table + " WHERE " + column + " = ? LIMIT 1",
                new String[]{value});
        String result = cursor.moveToFirst() ? cursor.getString(0) : null;
        cursor.close();
        return result;
    }
}
