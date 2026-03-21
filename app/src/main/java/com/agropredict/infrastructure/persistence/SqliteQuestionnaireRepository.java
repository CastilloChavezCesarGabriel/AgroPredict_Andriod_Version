package com.agropredict.infrastructure.persistence;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.request.data.Questionnaire;

public final class SqliteQuestionnaireRepository implements IQuestionnaireRepository {
    private final DatabaseHelper databaseHelper;

    public SqliteQuestionnaireRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void store(String diagnosticIdentifier, Questionnaire questionnaire) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        questionnaire.accept(new QuestionnaireRecorder(database, diagnosticIdentifier));
    }
}
