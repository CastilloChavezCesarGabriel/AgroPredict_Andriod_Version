package com.agropredict.application.request.data;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.component.user.Credential;
import com.agropredict.domain.component.user.UserProfile;

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

    public Credential hash(IPasswordHasher hasher) {
        return credential.hash(hasher);
    }

    public UserProfile establish(Registrant personal, ICatalogRepository catalog) {
        return profile.establish(personal, catalog);
    }
}