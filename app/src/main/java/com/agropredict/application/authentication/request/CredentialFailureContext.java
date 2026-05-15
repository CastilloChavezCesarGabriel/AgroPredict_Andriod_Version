package com.agropredict.application.authentication.request;

import com.agropredict.application.factory.failure.IEmailFailureFactory;
import com.agropredict.application.factory.failure.IPasswordFailureFactory;
import com.agropredict.application.input_validation.EmailValidator;
import com.agropredict.domain.input_validation.gate.IValidationGate;
import com.agropredict.application.input_validation.PasswordValidator;
import java.util.Objects;

public final class CredentialFailureContext {
    private final IEmailFailureFactory emailFactory;
    private final IPasswordFailureFactory passwordFactory;

    public CredentialFailureContext(IEmailFailureFactory emailFactory, IPasswordFailureFactory passwordFactory) {
        this.emailFactory = Objects.requireNonNull(emailFactory, "credential failure context requires an email factory");
        this.passwordFactory = Objects.requireNonNull(passwordFactory, "credential failure context requires a password factory");
    }

    public void check(String email, String password, IValidationGate gate) {
        new EmailValidator(emailFactory).check(email, gate);
        new PasswordValidator(passwordFactory).check(password, gate);
    }
}
