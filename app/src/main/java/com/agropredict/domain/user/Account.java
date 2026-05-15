package com.agropredict.domain.user;

import com.agropredict.domain.authentication.session.ISessionBuilder;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.user.visitor.ICredentialConsumer;
import com.agropredict.domain.user.visitor.IEmailConsumer;
import com.agropredict.domain.user.visitor.IOccupationConsumer;
import com.agropredict.domain.user.visitor.IUsernameConsumer;
import java.util.Objects;

public final class Account {
    private final String username;
    private final Credential credential;
    private final Occupation occupation;

    public Account(String username, Credential credential, Occupation occupation) {
        this.username = ArgumentPrecondition.validate(username, "account username");
        this.credential = Objects.requireNonNull(credential, "account requires a credential");
        this.occupation = Objects.requireNonNull(occupation, "account requires an occupation");
    }

    public void enroll(IUsernameConsumer consumer) {
        consumer.enroll(username);
    }

    public void authenticate(ICredentialConsumer consumer) {
        credential.authenticate(consumer);
    }

    public void mail(IEmailConsumer consumer) {
        credential.mail(consumer);
    }

    public void classify(IOccupationConsumer consumer) {
        occupation.classify(consumer);
    }

    public void expose(ISessionBuilder builder, String identifier) {
        occupation.expose(builder, identifier);
    }
}
