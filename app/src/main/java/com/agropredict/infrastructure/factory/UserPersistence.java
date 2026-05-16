package com.agropredict.infrastructure.factory;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.infrastructure.persistence.audit.AuditLogger;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.sync.SqliteSyncRecorder;
import com.agropredict.infrastructure.persistence.repository.SqliteUserRepository;
import com.agropredict.infrastructure.persistence.sync.SyncingUserRepository;
import com.agropredict.infrastructure.security.PasswordHasher;
import java.util.Objects;

public final class UserPersistence {
    private final Database database;
    private final SqliteRowFactory rowFactory;
    private final ISessionRepository session;

    public UserPersistence(Database database, SqliteRowFactory rowFactory, ISessionRepository session) {
        this.database = Objects.requireNonNull(database, "user persistence requires a database");
        this.rowFactory = Objects.requireNonNull(rowFactory, "user persistence requires a row factory");
        this.session = Objects.requireNonNull(session, "user persistence requires a session");
    }

    public IPasswordHasher createPasswordHasher() {
        return new PasswordHasher();
    }

    public IUserRepository createUserRepository() {
        SqliteUserRepository sqliteUserRepository = new SqliteUserRepository(database, createPasswordHasher(), rowFactory);
        return new SyncingUserRepository(sqliteUserRepository, new SqliteSyncRecorder(session, rowFactory));
    }

    public IAuditLogger createAuditLogger() {
        return new AuditLogger(rowFactory);
    }
}