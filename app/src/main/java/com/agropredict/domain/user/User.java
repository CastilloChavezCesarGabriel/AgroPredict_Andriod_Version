package com.agropredict.domain.user;

import com.agropredict.domain.identifier.IIdentifierConsumer;
import com.agropredict.domain.authentication.session.ISessionBuilder;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.user.visitor.ICredentialConsumer;
import com.agropredict.domain.user.visitor.IEmailConsumer;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;
import java.util.Objects;

public final class User implements IUser, ISessionSubject {
    private final String identifier;
    private final ContactInformation contactInformation;
    private final Account account;

    public User(String identifier, ContactInformation contactInformation, Account account) {
        this.identifier = ArgumentPrecondition.validate(identifier, "user identifier");
        this.contactInformation = Objects.requireNonNull(contactInformation,
                "user requires contact information");
        this.account = Objects.requireNonNull(account, "user requires an account");
    }

    @Override
    public void describe(IUserIdentityConsumer consumer) {
        contactInformation.describe(consumer, identifier);
    }

    @Override
    public void contact(IPhoneConsumer consumer) {
        contactInformation.contact(consumer);
    }

    @Override
    public void enroll(IUsernameConsumer consumer) {
        account.enroll(consumer);
    }

    @Override
    public void authenticate(ICredentialConsumer consumer) {
        account.authenticate(consumer);
    }

    @Override
    public void mail(IEmailConsumer consumer) {
        account.mail(consumer);
    }

    @Override
    public void classify(IOccupationConsumer consumer) {
        account.classify(consumer);
    }

    @Override
    public void identify(IIdentifierConsumer consumer) {
        consumer.identify(identifier);
    }

    @Override
    public boolean expose(ISessionBuilder builder) {
        account.expose(builder, identifier);
        return true;
    }
}