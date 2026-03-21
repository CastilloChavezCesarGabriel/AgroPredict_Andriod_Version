package com.agropredict.application.request.data;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.domain.component.user.Credential;
import com.agropredict.domain.component.user.UserData;
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

    public UserData compile(Registrant personal, ICatalogRepository catalog) {
        Credential hashed = credential.hash();
        UserProfile userProfile = profile.establish(personal, catalog);
        return new UserData(hashed, userProfile);
    }
}