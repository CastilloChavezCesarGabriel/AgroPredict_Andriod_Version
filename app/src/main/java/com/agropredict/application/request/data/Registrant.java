package com.agropredict.application.request.data;

import com.agropredict.application.request.RegistrationException;
import com.agropredict.domain.component.user.UserContact;
import com.agropredict.domain.component.user.UserIdentity;
import com.agropredict.domain.validation.FullNameValidator;
import com.agropredict.domain.validation.PhoneNumberValidator;

public final class Registrant {
    private final String fullName;
    private final String phone;

    public Registrant(String fullName, String phone) {
        this.fullName = fullName;
        this.phone = phone;
    }

    public void validate() {
        if (!new FullNameValidator().validate(fullName))
            throw new RegistrationException("Nombre completo invalido");
        if (!new PhoneNumberValidator().validate(phone))
            throw new RegistrationException("Numero de telefono invalido");
    }

    public UserIdentity identify() {
        String identifier = "user_" + System.currentTimeMillis();
        return new UserIdentity(identifier, fullName);
    }

    public UserContact enroll(String username) {
        return new UserContact(username, phone);
    }
}