package com.agropredict.repository;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.user_registration.RegistrationException;
import com.agropredict.application.request.user_registration.RegistrationRequest;
import com.agropredict.domain.Session;

public final class ScriptedUserRepository implements IUserRepository {
    private final String rejectionMessage;

    public ScriptedUserRepository(String rejectionMessage) {
        this.rejectionMessage = rejectionMessage;
    }

    @Override
    public Session authenticate(String email, String password) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void register(RegistrationRequest request, ICatalogRepository catalog) {
        if (rejectionMessage != null) throw new RegistrationException(rejectionMessage);
    }

    @Override
    public boolean reset(String email, String passwordHash) {
        throw new UnsupportedOperationException();
    }
}
