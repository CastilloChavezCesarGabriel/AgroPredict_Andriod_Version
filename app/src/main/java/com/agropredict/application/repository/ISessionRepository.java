package com.agropredict.application.repository;

import com.agropredict.domain.authentication.ISession;
import com.agropredict.domain.authentication.Session;

public interface ISessionRepository {
    void save(Session session);
    ISession recall();
    void clear();
}