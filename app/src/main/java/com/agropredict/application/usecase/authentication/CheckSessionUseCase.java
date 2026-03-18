package com.agropredict.application.usecase.authentication;

import com.agropredict.application.consumer.ISessionResultConsumer;
import com.agropredict.application.repository.ISessionRepository;

public final class CheckSessionUseCase {
    private final ISessionRepository sessionRepository;

    public CheckSessionUseCase(ISessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void check(ISessionResultConsumer visitor) {
        String userIdentifier = sessionRepository.load();
        boolean hasSession = userIdentifier != null && !userIdentifier.isEmpty();
        visitor.visit(hasSession, userIdentifier);
    }
}