package com.agropredict.application.authentication.usecase;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.authentication.ISessionConsumer;
import java.util.Objects;

public final class CheckSessionUseCase {
    private final ISessionRepository sessionRepository;

    public CheckSessionUseCase(ISessionRepository sessionRepository) {
        this.sessionRepository = Objects.requireNonNull(sessionRepository, "check session use case requires a session repository");
    }

    public void check(ISessionConsumer consumer) {
        sessionRepository.recall().report(consumer);
    }
}
