package com.agropredict.application.authentication.request;

import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.input_validation.gate.ValidationGate;
import com.agropredict.domain.user.visitor.ICredentialConsumer;
import java.util.Objects;

public final class CredentialDraft {
    private final String email;
    private final String password;
    private final CredentialFailureContext failureContext;

    public CredentialDraft(String email, String password, CredentialFailureContext failureContext) {
        this.email = Objects.requireNonNull(email, "credential draft requires an email");
        this.password = Objects.requireNonNull(password, "credential draft requires a password");
        this.failureContext = Objects.requireNonNull(failureContext, "credential draft requires a failure context");
    }

    public void validate() {
        failureContext.check(email, password, new ValidationGate());
    }

    public void submit(ICredentialConsumer consumer, IPasswordHasher hasher) {
        consumer.authenticate(email, hasher.hash(password));
    }
}
