package com.agropredict.infrastructure.persistence.visitor;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.visitor.IQuestionnaireVisitor;
import com.agropredict.domain.Identifier;

public final class QuestionnairePersistenceVisitor implements IQuestionnaireVisitor {
    private final SQLiteDatabase database;
    private final String diagnosticIdentifier;

    public QuestionnairePersistenceVisitor(SQLiteDatabase database, String diagnosticIdentifier) {
        this.database = database;
        this.diagnosticIdentifier = diagnosticIdentifier;
    }

    @Override
    public void visitEnvironment(String temperature, String humidity) {
        record("temperature", temperature);
        record("humidity", humidity);
    }

    @Override
    public void visitRain(String precipitation) {
        record("rain", precipitation);
    }

    @Override
    public void visitSoil(String moisture, String acidity) {
        record("soilMoisture", moisture);
        record("ph", acidity);
    }

    @Override
    public void visitIrrigation(String irrigation, String fertilization) {
        record("irrigation", irrigation);
        record("fertilization", fertilization);
    }

    @Override
    public void visitPestControl(String spraying, String weeds) {
        record("spraying", spraying);
        record("weeds", weeds);
    }

    @Override
    public void visitSymptom(String symptomType, String severity) {
        record("symptom", symptomType);
        record("severity", severity);
    }

    @Override
    public void visitPest(String insects, String animals) {
        record("insects", insects);
        record("animals", animals);
    }

    private void record(String key, String value) {
        String questionId = resolve(key);
        insert(questionId, key, value);
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
        database.insert("ai_user_response", null, values);
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