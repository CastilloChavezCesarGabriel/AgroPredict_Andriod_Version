package com.agropredict.domain.user;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.user.visitor.ICredentialConsumer;
import com.agropredict.domain.user.visitor.IEmailConsumer;

public final class Credential {
    private final String email;
    private final String password;

    public Credential(String email, String password) {
        this.email = ArgumentPrecondition.validate(email, "credential email");
        this.password = ArgumentPrecondition.validate(password, "credential password");
    }

    public void authenticate(ICredentialConsumer consumer) {
        consumer.authenticate(email, password);
    }

    public void mail(IEmailConsumer consumer) {
        consumer.mail(email);
    }
}