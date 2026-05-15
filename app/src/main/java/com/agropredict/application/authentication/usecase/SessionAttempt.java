package com.agropredict.application.authentication.usecase;

import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.authentication.session.ISessionBuilder;
import com.agropredict.domain.authentication.session.Session;
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
