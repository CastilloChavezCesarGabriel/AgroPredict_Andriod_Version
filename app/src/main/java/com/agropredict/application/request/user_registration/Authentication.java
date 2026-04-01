package com.agropredict.application.request.user_registration;

import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.service.IPasswordHasher;
import com.agropredict.domain.input_validation.EmailValidator;
import com.agropredict.domain.input_validation.PasswordValidator;
import com.agropredict.domain.visitor.user.IUserVisitor;

public final class Authentication {
    private final String email;
    private final String password;

    public Authentication(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void validate(IUserRepository repository) {
        if (!new EmailValidator().isValid(email))
            throw new RegistrationException("Invalid email address");
        if (!new PasswordValidator().isValid(password))
            throw new RegistrationException("Invalid password");
        if (repository.isRegistered(email))
            throw new RegistrationException("This email is already registered");
    }

    public void authenticate(IUserVisitor visitor, IPasswordHasher hasher) {
        visitor.visitCredential(email, hasher.hash(password));
    }
}