package com.agropredict.infrastructure.factory;

import com.agropredict.application.factory.IAccessFactory;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.application.service.IPasswordHasher;
import java.util.Objects;

public final class AndroidAccessFactory implements IAccessFactory {
    private final UserPersistence userPersistence;
    private final SessionPersistence sessionPersistence;
    private final CatalogPersistence catalogPersistence;

    public AndroidAccessFactory(UserPersistence userPersistence, SessionPersistence sessionPersistence, CatalogPersistence catalogPersistence) {
        this.userPersistence = Objects.requireNonNull(userPersistence,
                "android access factory requires a user persistence");
        this.sessionPersistence = Objects.requireNonNull(sessionPersistence,
                "android access factory requires a session persistence");
        this.catalogPersistence = Objects.requireNonNull(catalogPersistence,
                "android access factory requires a catalog persistence");
    }

    @Override
    public IUserRepository createUserRepository() {
        return userPersistence.createUserRepository();
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return sessionPersistence.createSessionRepository();
    }

    @Override
    public IPasswordHasher createPasswordHasher() {
        return userPersistence.createPasswordHasher();
    }

    @Override
    public IAuditLogger createAuditLogger() {
        return userPersistence.createAuditLogger();
    }

    @Override
    public ICatalogRepository createOccupationCatalog() {
        return catalogPersistence.createOccupationCatalog();
    }
}