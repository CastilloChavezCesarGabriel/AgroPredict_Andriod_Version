package com.agropredict.infrastructure.persistence.visitor;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.visitor.IQuestionnaireVisitor;
import com.agropredict.domain.Identifier;
import com.agropredict.infrastructure.persistence.database.Clock;
import com.agropredict.infrastructure.persistence.schema.IKeyConsumer;
import com.agropredict.infrastructure.persistence.schema.QuestionKey;

public final class QuestionnairePersistenceVisitor implements IQuestionnaireVisitor, IKeyConsumer {
    private final SQLiteDatabase database;
    private final String diagnosticIdentifier;

    public QuestionnairePersistenceVisitor(SQLiteDatabase database, String diagnosticIdentifier) {
        this.database = database;
        this.diagnosticIdentifier = diagnosticIdentifier;
    }

    @Override
    public void visitEnvironment(String temperature, String humidity) {
        QuestionKey.TEMPERATURE.pair(this, temperature);
        QuestionKey.HUMIDITY.pair(this, humidity);
    }

    @Override
    public void visitRain(String precipitation) {
        QuestionKey.RAIN.pair(this, precipitation);
    }

    @Override
    public void visitSoil(String moisture, String acidity) {
        QuestionKey.SOIL_MOISTURE.pair(this, moisture);
        QuestionKey.PH.pair(this, acidity);
    }

    @Override
    public void visitIrrigation(String irrigation, String fertilization) {
        QuestionKey.IRRIGATION.pair(this, irrigation);
        QuestionKey.FERTILIZATION.pair(this, fertilization);
    }

    @Override
    public void visitPestControl(String spraying, String weeds) {
        QuestionKey.SPRAYING.pair(this, spraying);
        QuestionKey.WEEDS.pair(this, weeds);
    }

    @Override
    public void visitSymptom(String symptomType, String severity) {
        QuestionKey.SYMPTOM.pair(this, symptomType);
        QuestionKey.SEVERITY.pair(this, severity);
    }

    @Override
    public void visitPest(String insects, String animals) {
        QuestionKey.INSECTS.pair(this, insects);
        QuestionKey.ANIMALS.pair(this, animals);
    }

    @Override
    public void accept(String key, String value) {
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
        values.put("id", Identifier.generate(key));
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