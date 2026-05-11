package com.agropredict.application.authentication.request;

import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.input_validation.EmailValidator;
import com.agropredict.domain.input_validation.PasswordValidator;
import com.agropredict.domain.input_validation.ValidationGate;
import com.agropredict.domain.user.visitor.ICredentialConsumer;
import java.util.Objects;

public final class Credential {
    private final String email;
    private final String password;

    public Credential(String email, String password) {
        this.email = Objects.requireNonNull(email, "credential requires an email");
        this.password = Objects.requireNonNull(password, "credential requires a password");
    }

    public void validate() {
        ValidationGate gate = new ValidationGate();
        new EmailValidator().check(email, gate);
        new PasswordValidator().check(password, gate);
    }

    public void submit(ICredentialConsumer consumer, IPasswordHasher hasher) {
        consumer.authenticate(email, hasher.hash(password));
    }
}
