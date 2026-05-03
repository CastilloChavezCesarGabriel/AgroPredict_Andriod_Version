package com.agropredict.application.request.user_registration;

import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.IIdentifierConsumer;
import com.agropredict.domain.Identifier;
import com.agropredict.domain.visitor.user.IUserVisitor;

public final class RegistrationRequest {
    private final Registrant personal;
    private final Account account;
    private final String identifier;

    public RegistrationRequest(Registrant personal, Account account) {
        this.personal = personal;
        this.account = account;
        this.identifier = Identifier.generate("user");
    }

    public void validate() {
        personal.validate();
        account.validate();
    }

    public void authenticate(IUserVisitor visitor, IPasswordHasher hasher) {
        personal.dispatch(visitor, identifier);
        account.authenticate(visitor, hasher);
        account.enroll(visitor);
    }

    public void classify(IUserVisitor visitor, ICatalogRepository catalog) {
        account.classify(visitor, catalog);
    }

    public void identify(IIdentifierConsumer consumer) {
        consumer.accept(identifier);
    }
}
