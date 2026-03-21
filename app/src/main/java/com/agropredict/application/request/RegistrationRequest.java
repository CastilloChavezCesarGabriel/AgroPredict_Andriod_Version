package com.agropredict.application.request;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.data.Account;
import com.agropredict.application.request.data.Registrant;
import com.agropredict.domain.component.user.UserData;
import com.agropredict.domain.component.user.UserIdentity;
import com.agropredict.domain.entity.User;

public final class RegistrationRequest {
    private final Registrant personal;
    private final Account account;

    public RegistrationRequest(Registrant personal, Account account) {
        this.personal = personal;
        this.account = account;
    }

    public void register(IUserRepository repository, ICatalogRepository catalog) {
        personal.validate();
        account.validate(repository);
        UserIdentity identity = personal.identify();
        UserData data = account.compile(personal, catalog);
        repository.store(User.create(identity, data));
    }
}