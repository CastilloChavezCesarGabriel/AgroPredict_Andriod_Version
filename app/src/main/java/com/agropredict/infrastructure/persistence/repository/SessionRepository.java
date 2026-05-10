package com.agropredict.infrastructure.persistence.repository;

import android.content.Context;
import android.content.SharedPreferences;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.domain.authentication.ISession;
import com.agropredict.domain.authentication.NoSession;
import com.agropredict.domain.authentication.Session;
import com.agropredict.domain.authentication.ISessionConsumer;

public final class SessionRepository implements ISessionRepository, ISessionConsumer {
    private static final String USER_IDENTIFIER_KEY = "logged_user_id";
    private static final String OCCUPATION_KEY = "logged_occupation";
    private static final String TIMESTAMP_KEY = "session_timestamp";
    private static final long EXPIRATION_MILLIS = 30 * 60 * 1000;
    private final SharedPreferences preferences;

    public SessionRepository(Context context) {
        this.preferences = context.getSharedPreferences("agropredict_session", Context.MODE_PRIVATE);
    }

    @Override
    public void save(Session session) {
        session.report(this);
    }

    @Override
    public void report(String userIdentifier, String occupation) {
        preferences.edit()
                .putString(USER_IDENTIFIER_KEY, userIdentifier)
                .putString(OCCUPATION_KEY, occupation)
                .putLong(TIMESTAMP_KEY, System.currentTimeMillis())
                .apply();
    }

    @Override
    public ISession recall() {
        long timestamp = preferences.getLong(TIMESTAMP_KEY, 0);
        if (timestamp == 0 || isExpired(timestamp)) {
            clear();
            return new NoSession();
        }
        String identifier = preferences.getString(USER_IDENTIFIER_KEY, null);
        if (identifier == null || identifier.isEmpty()) {
            return new NoSession();
        }
        refresh();
        String occupation = preferences.getString(OCCUPATION_KEY, null);
        return new Session(identifier, occupation);
    }

    @Override
    public void clear() {
        preferences.edit().clear().apply();
    }

    private boolean isExpired(long timestamp) {
        return System.currentTimeMillis() - timestamp > EXPIRATION_MILLIS;
    }

    private void refresh() {
        preferences.edit()
                .putLong(TIMESTAMP_KEY, System.currentTimeMillis())
                .apply();
    }
}