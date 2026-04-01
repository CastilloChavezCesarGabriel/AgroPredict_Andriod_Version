package com.agropredict.application.repository;

import com.agropredict.domain.visitor.session.ISessionVisitor;
import com.agropredict.domain.Session;

public interface ISessionRepository extends ISessionVisitor {
    Session recall();
    void clear();
}