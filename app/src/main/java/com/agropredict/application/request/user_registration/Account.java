package com.agropredict.application.request.user_registration;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.visitor.user.IUserVisitor;

public final class Account {
    private final Authentication credential;
    private final Profile profile;

    public Account(Authentication credential, Profile profile) {
        this.credential = credential;
        this.profile = profile;
    }

    public void validate(IUserRepository repository) {
        credential.validate(repository);
        profile.validate(repository);
    }

    public void authenticate(IUserVisitor visitor, IPasswordHasher hasher) {
        credential.authenticate(visitor, hasher);
    }

    public void enroll(IUserVisitor visitor) {
        profile.dispatch(visitor);
    }

    public void classify(IUserVisitor visitor, ICatalogRepository catalog) {
        profile.classify(visitor, catalog);
    }
}