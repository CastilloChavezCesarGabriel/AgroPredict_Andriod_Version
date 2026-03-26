package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.ISessionVisitor;
import com.agropredict.domain.Session;

public final class CheckSessionUseCase {
    private final ISessionRepository sessionRepository;

    public CheckSessionUseCase(ISessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void check(ISessionVisitor visitor) {
        Session session = sessionRepository.recall();
        session.accept(visitor);
    }
}
