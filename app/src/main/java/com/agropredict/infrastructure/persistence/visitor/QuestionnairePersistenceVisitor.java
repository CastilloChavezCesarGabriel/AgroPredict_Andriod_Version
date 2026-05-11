package com.agropredict.infrastructure.persistence.visitor;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.IAnswerConsumer;
import com.agropredict.domain.identifier.IdentifierFactory;
import com.agropredict.infrastructure.persistence.database.Clock;

public final class QuestionnairePersistenceVisitor implements IAnswerConsumer {
    private final SQLiteDatabase database;
    private final String diagnosticIdentifier;

    public QuestionnairePersistenceVisitor(SQLiteDatabase database, String diagnosticIdentifier) {
        this.database = database;
        this.diagnosticIdentifier = diagnosticIdentifier;
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
        ContentValues values = new ContentValues();
        values.put("id", IdentifierFactory.generate(key));
        values.put("diagnostic_id", diagnosticIdentifier);
        values.put("question_id", questionId);
        values.put("option_id", locate("ai_option", "option_text", value));
        values.put("text_value", value);
        values.put("created_at", Clock.read());
        database.insert("ai_user_response", null, values);
    }

    private void propagate(String key, String value) {
        if ("temperature".equals(key)) {
            database.execSQL("UPDATE diagnostic SET temperature = ? WHERE id = ?",
                    new Object[]{value, diagnosticIdentifier});
        } else if ("humidity".equals(key)) {
            database.execSQL("UPDATE diagnostic SET humidity = ? WHERE id = ?",
                    new Object[]{value, diagnosticIdentifier});
        }
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