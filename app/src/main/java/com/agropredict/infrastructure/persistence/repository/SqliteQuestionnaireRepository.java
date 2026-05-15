package com.agropredict.infrastructure.persistence.repository;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.application.diagnostic_submission.ai_questionnaire.Questionnaire;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.visitor.QuestionnairePersistenceVisitor;
import java.util.Objects;

public final class SqliteQuestionnaireRepository implements IQuestionnaireRepository {
    private final Database database;
    private final SqliteRowFactory rowFactory;

    public SqliteQuestionnaireRepository(Database database, SqliteRowFactory rowFactory) {
        this.database = Objects.requireNonNull(database, "questionnaire repository requires a database");
        this.rowFactory = Objects.requireNonNull(rowFactory, "questionnaire repository requires a row factory");
    }

    @Override
    public void store(String diagnosticIdentifier, Questionnaire questionnaire) {
        SQLiteDatabase database = this.database.getWritableDatabase();
        questionnaire.accept(new QuestionnairePersistenceVisitor(rowFactory, database, diagnosticIdentifier));
    }
}
