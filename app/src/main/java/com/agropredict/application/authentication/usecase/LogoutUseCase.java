package com.agropredict.application.authentication.usecase;

import com.agropredict.application.repository.ISessionRepository;
import java.util.Objects;

public final class LogoutUseCase {
    private final ISessionRepository sessionRepository;

    public LogoutUseCase(ISessionRepository sessionRepository) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository, "logout use case requires a session repository");
    }

    public void logout() {
        sessionRepository.clear();
    }
}