package com.agropredict.infrastructure.factory;

import android.content.Context;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import java.util.Objects;

public final class SessionPersistence {
    private final Context context;

    public SessionPersistence(Context context) {
        this.context = Objects.requireNonNull(context, "session persistence requires a context");
    }

    public ISessionRepository createSessionRepository() {
        return new SessionRepository(context);
    }
}