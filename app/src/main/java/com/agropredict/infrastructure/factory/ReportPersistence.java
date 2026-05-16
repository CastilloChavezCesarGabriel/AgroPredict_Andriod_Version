package com.agropredict.infrastructure.factory;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.report.SqliteReportRepository;
import com.agropredict.infrastructure.persistence.sync.SqliteSyncRecorder;
import com.agropredict.infrastructure.persistence.sync.SyncingReportRepository;
import java.util.Objects;

public final class ReportPersistence {
    private final Database database;
    private final SqliteRowFactory rowFactory;
    private final ISessionRepository session;

    public ReportPersistence(Database database, SqliteRowFactory rowFactory, ISessionRepository session) {
        this.database = Objects.requireNonNull(database, "report persistence requires a database");
        this.rowFactory = Objects.requireNonNull(rowFactory, "report persistence requires a row factory");
        this.session = Objects.requireNonNull(session, "report persistence requires a session");
    }

    public IReportRepository createReportRepository() {
        return new SyncingReportRepository(
                new SqliteReportRepository(database, rowFactory),
                new SqliteSyncRecorder(session, rowFactory));
    }
}