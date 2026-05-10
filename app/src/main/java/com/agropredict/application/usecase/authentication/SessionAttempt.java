package com.agropredict.application.usecase.authentication;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.authentication.ISessionBuilder;
import com.agropredict.domain.authentication.Session;
import java.util.Objects;

public final class SessionAttempt implements ISessionBuilder {
    private final ISessionRepository sessions;

    public SessionAttempt(ISessionRepository sessions) {
        this.sessions = Objects.requireNonNull(sessions, "session attempt requires a session repository");
    }

    @Override
    public void build(String identifier, String occupation) {
        sessions.save(new Session(identifier, occupation));
    }
}
