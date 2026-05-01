package com.agropredict.application.repository;

import com.agropredict.domain.Session;

public interface ISessionRepository {
    void save(Session session);
    Session recall();
    void clear();
}
