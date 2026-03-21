package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.visitor.IQuestionnaireVisitor;

public final class QuestionnaireRecorder implements IQuestionnaireVisitor {
    private final SQLiteDatabase database;
    private final String diagnosticIdentifier;

    public QuestionnaireRecorder(SQLiteDatabase database, String diagnosticIdentifier) {
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
        ContentValues values = new ContentValues();
        values.put("id", key + "_" + System.currentTimeMillis());
        values.put("diagnostic_id", diagnosticIdentifier);
        values.put("question_id", key);
        values.put("text_value", value);
        database.insert("ai_user_response", null, values);
    }
}
