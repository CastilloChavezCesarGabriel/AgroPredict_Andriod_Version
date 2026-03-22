package com.agropredict.infrastructure.persistence.repository;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.request.data.Questionnaire;
import com.agropredict.infrastructure.persistence.Database;
import com.agropredict.infrastructure.persistence.visitor.QuestionnairePersistenceVisitor;

public final class SqliteQuestionnaireRepository implements IQuestionnaireRepository {
    private final Database database;

    public SqliteQuestionnaireRepository(Database database) {
        this.database = database;
    }

    @Override
    public void store(String diagnosticIdentifier, Questionnaire questionnaire) {
        SQLiteDatabase database = this.database.getWritableDatabase();
        questionnaire.accept(new QuestionnairePersistenceVisitor(database, diagnosticIdentifier));
    }
}