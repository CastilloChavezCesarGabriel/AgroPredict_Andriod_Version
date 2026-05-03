package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.domain.Session;

public final class SyncingUserRepository implements IUserRepository {
    private final IUserRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingUserRepository(IUserRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = delegate;
        this.recorder = recorder;
    }

    @Override
    public Session authenticate(String email, String password) {
        return delegate.authenticate(email, password);
    }

    @Override
    public void register(RegistrationRequest request, ICatalogRepository catalog) {
        delegate.register(request, catalog);
        request.identify(identifier -> recorder.insert("user", identifier));
    }

    @Override
    public boolean reset(String email, String passwordHash) {
        boolean completed = delegate.reset(email, passwordHash);
        if (completed) recorder.update("user", email);
        return completed;
    }
}
