package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.factory.IReviewFactory;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteDiagnosticRepository;

public final class AndroidReviewFactory implements IReviewFactory {
    private final Database database;
    private final Context context;

    public AndroidReviewFactory(Database database, Context context) {
        this.database = database;
        this.context = context;
    }

    @Override
    public IDiagnosticRepository createDiagnosticRepository() {
        return new SqliteDiagnosticRepository(database);
    }

    @Override
    public ICropRepository createCropRepository() {
        return new SqliteCropRepository(database, createSessionRepository());
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return new SessionRepository(context);
    }
}