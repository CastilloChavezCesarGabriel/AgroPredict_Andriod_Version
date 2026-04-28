package com.agropredict.application.factory;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IAuditLogger;
import com.agropredict.application.service.IPasswordHasher;

public interface IAccessFactory {
    IUserRepository createUserRepository();
    ISessionRepository createSessionRepository();
    IPasswordHasher createPasswordHasher();
    IAuditLogger createAuditLogger();
    ICatalogRepository createOccupationCatalog();
}