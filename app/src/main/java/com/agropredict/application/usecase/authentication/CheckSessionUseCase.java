package com.agropredict.application.usecase.authentication;

import com.agropredict.application.visitor.ISessionResultVisitor;
import com.agropredict.application.repository.ISessionRepository;

public final class CheckSessionUseCase {
    private final ISessionRepository sessionRepository;

    public CheckSessionUseCase(ISessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void check(ISessionResultVisitor visitor) {
        String userIdentifier = sessionRepository.recall();
        boolean hasSession = userIdentifier != null && !userIdentifier.isEmpty();
        visitor.visit(hasSession, userIdentifier);
    }
}