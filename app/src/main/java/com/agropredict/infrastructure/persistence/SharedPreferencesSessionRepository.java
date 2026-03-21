package com.agropredict.infrastructure.persistence;

import android.content.Context;
import android.content.SharedPreferences;

import com.agropredict.application.repository.ISessionRepository;

public final class SharedPreferencesSessionRepository implements ISessionRepository {
    private static final String PREFERENCES_NAME = "agropredict_session";
    private static final String USER_IDENTIFIER_KEY = "logged_user_id";
    private final SharedPreferences preferences;

    public SharedPreferencesSessionRepository(Context context) {
        this.preferences = context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    @Override
    public void save(String userIdentifier) {
        preferences.edit().putString(USER_IDENTIFIER_KEY, userIdentifier).apply();
    }

    @Override
    public String recall() {
        return preferences.getString(USER_IDENTIFIER_KEY, null);
    }

    @Override
    public void clear() {
        preferences.edit().remove(USER_IDENTIFIER_KEY).apply();
    }
}