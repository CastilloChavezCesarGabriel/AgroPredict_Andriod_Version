package com.agropredict.application.request.data;

import com.agropredict.application.Hasher;
import com.agropredict.application.repository.IUserRepository;
import com.agropredict.application.request.RegistrationException;
import com.agropredict.domain.component.user.Credential;
import com.agropredict.domain.validation.EmailValidator;
import com.agropredict.domain.validation.PasswordValidator;

public final class Authentication {
    private final String email;
    private final String password;

    public Authentication(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public void validate(IUserRepository repository) {
        if (!new EmailValidator().validate(email))
            throw new RegistrationException("Correo electronico invalido");
        if (!new PasswordValidator().validate(password))
            throw new RegistrationException("Contrasena invalida");
        if (repository.isRegistered(email))
            throw new RegistrationException("El correo ya esta registrado");
    }

    public Credential hash() {
        String passwordHash = new Hasher().hash(password);
        return new Credential(email, passwordHash);
    }
}