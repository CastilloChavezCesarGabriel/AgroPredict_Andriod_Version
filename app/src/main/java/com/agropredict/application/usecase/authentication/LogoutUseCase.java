package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.ISessionRepository;

public final class LogoutUseCase {
    private final ISessionRepository sessionRepository;

    public LogoutUseCase(ISessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    public void logout() {
        sessionRepository.clear();
    }
}