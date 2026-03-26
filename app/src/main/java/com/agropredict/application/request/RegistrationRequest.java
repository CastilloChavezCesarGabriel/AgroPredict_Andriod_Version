package com.agropredict.application.request;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.data.Account;
import com.agropredict.application.request.data.Registrant;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.component.user.Credential;
import com.agropredict.domain.component.user.UserData;
import com.agropredict.domain.component.user.UserIdentity;
import com.agropredict.domain.component.user.UserProfile;
import com.agropredict.domain.entity.User;

public final class RegistrationRequest {
    private final Registrant personal;
    private final Account account;

    public RegistrationRequest(Registrant personal, Account account) {
        this.personal = personal;
        this.account = account;
    }

    public void validate(IUserRepository repository) {
        personal.validate();
        account.validate(repository);
    }

    public User compile(IPasswordHasher hasher, ICatalogRepository catalog) {
        UserIdentity identity = personal.identify();
        Credential credential = account.hash(hasher);
        UserProfile profile = account.establish(personal, catalog);
        return User.create(identity, new UserData(credential, profile));
    }
}
