package com.agropredict.infrastructure.composer;

import android.content.Context;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.infrastructure.persistence.AuditLogger;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteUserRepository;
import com.agropredict.infrastructure.persistence.schema.CatalogName;
import com.agropredict.infrastructure.security.PasswordHasher;

public final class AccessComposer implements IAccessFactory {
    private final Database database;
    private final Context context;

    public AccessComposer(Database database, Context context) {
        this.database = database;
        this.context = context;
    }

    @Override
    public IUserRepository createUserRepository() {
        return new SqliteUserRepository(database, createOccupationCatalog(), createPasswordHasher());
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return new SessionRepository(context);
    }

    @Override
    public IPasswordHasher createPasswordHasher() {
        return new PasswordHasher();
    }

    @Override
    public IAuditLogger createAuditLogger() {
        return new AuditLogger(database);
    }

    @Override
    public ICatalogRepository createOccupationCatalog() {
        return CatalogName.OCCUPATION.open(database);
    }
}