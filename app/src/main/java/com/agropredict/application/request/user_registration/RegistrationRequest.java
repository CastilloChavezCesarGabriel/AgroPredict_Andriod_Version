package com.agropredict.application.request.user_registration;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.Identifier;
import com.agropredict.domain.visitor.user.IUserVisitor;

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

    public void authenticate(IUserVisitor visitor, IPasswordHasher hasher) {
        String identifier = Identifier.generate("user");
        personal.dispatch(visitor, identifier);
        account.authenticate(visitor, hasher);
        account.enroll(visitor);
    }

    public void classify(IUserVisitor visitor, ICatalogRepository catalog) {
        account.classify(visitor, catalog);
    }
}