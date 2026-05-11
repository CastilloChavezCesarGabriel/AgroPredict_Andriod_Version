package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IPhotographRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.domain.diagnostic.ISeverityFactory;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.repository.DiagnosticContext;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteDiagnosticRepository;
import com.agropredict.infrastructure.persistence.repository.SqlitePhotographRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteSyncRecorder;
import com.agropredict.infrastructure.persistence.repository.SyncingCropRepository;
import com.agropredict.infrastructure.persistence.repository.SyncingDiagnosticRepository;
import com.agropredict.infrastructure.persistence.repository.SyncingPhotographRepository;

public final class AndroidReviewFactory implements IReviewFactory {
    private final Database database;
    private final Context context;
    private final ISeverityFactory severityFactory;

    public AndroidReviewFactory(Database database, Context context, ISeverityFactory severityFactory) {
        this.database = database;
        this.context = context;
        this.severityFactory = severityFactory;
    }

    @Override
    public IDiagnosticRepository createDiagnosticRepository() {
        DiagnosticContext context = new DiagnosticContext(createSessionRepository(), severityFactory);
        return new SyncingDiagnosticRepository(
                new SqliteDiagnosticRepository(database, context),
                createSyncRecorder());
    }

    @Override
    public ICropRepository createCropRepository() {
        return new SyncingCropRepository(
                new SqliteCropRepository(database, createSessionRepository()),
                createSyncRecorder());
    }

    @Override
    public IPhotographRepository createPhotographRepository() {
        return new SyncingPhotographRepository(
                new SqlitePhotographRepository(database, createSessionRepository()),
                createSyncRecorder());
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return new SessionRepository(context);
    }

    private SqliteSyncRecorder createSyncRecorder() {
        return new SqliteSyncRecorder(database, createSessionRepository());
    }
}
