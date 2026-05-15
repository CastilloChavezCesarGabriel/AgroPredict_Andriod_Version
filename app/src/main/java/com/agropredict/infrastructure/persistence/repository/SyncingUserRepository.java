package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.authentication.request.RegistrationRequest;
import com.agropredict.domain.user.ISessionSubject;
import com.agropredict.domain.user.IUser;
import java.util.Objects;

public final class SyncingUserRepository implements IUserRepository {
    private final IUserRepository delegate;
    private final SqliteSyncRecorder recorder;

    public SyncingUserRepository(IUserRepository delegate, SqliteSyncRecorder recorder) {
        this.delegate = Objects.requireNonNull(delegate, "syncing user repository requires a delegate");
        this.recorder = Objects.requireNonNull(recorder, "syncing user repository requires a sync recorder");
    }

    @Override
    public ISessionSubject authenticate(String email, String password) {
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

    @Override
    public IUser find(String userIdentifier) {
        return delegate.find(userIdentifier);
    }
}
