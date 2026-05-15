package com.agropredict.application.repository;

import com.agropredict.domain.authentication.session.ISession;
import com.agropredict.domain.authentication.session.Session;

public interface ISessionRepository {
    void save(Session session);
    ISession recall();
    void clear();
}