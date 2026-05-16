package com.agropredict.infrastructure.factory;

import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IQuestionnaireRepository;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.repository.DiagnosticPersistenceContext;
import com.agropredict.infrastructure.persistence.repository.SqliteDiagnosticRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteQuestionnaireRepository;
import com.agropredict.infrastructure.persistence.sync.SyncingDiagnosticRepository;
import java.util.Objects;

public final class DiagnosticPersistence {
    private final Database database;
    private final SqliteRowFactory rowFactory;
    private final DiagnosticPersistenceContext context;

    public DiagnosticPersistence(Database database, SqliteRowFactory rowFactory, DiagnosticPersistenceContext context) {
        this.database = Objects.requireNonNull(database, "diagnostic persistence requires a database");
        this.rowFactory = Objects.requireNonNull(rowFactory, "diagnostic persistence requires a row factory");
        this.context = Objects.requireNonNull(context, "diagnostic persistence requires a diagnostic context");
    }

    public IDiagnosticRepository createDiagnosticRepository() {
        SqliteDiagnosticRepository sqliteDiagnostic = new SqliteDiagnosticRepository(database, context, rowFactory);
        return new SyncingDiagnosticRepository(sqliteDiagnostic, sqliteDiagnostic,
                context.track(rowFactory));
    }

    public IQuestionnaireRepository createQuestionnaireRepository() {
        return new SqliteQuestionnaireRepository(database, rowFactory);
    }
}