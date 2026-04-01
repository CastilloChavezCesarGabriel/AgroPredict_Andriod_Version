package com.agropredict.application.request.user_registration;

import com.agropredict.domain.input_validation.FullNameValidator;
import com.agropredict.domain.input_validation.PhoneNumberValidator;
import com.agropredict.domain.visitor.user.IUserVisitor;

public final class Registrant {
    private final String fullName;
    private final String phoneNumber;

    public Registrant(String fullName, String phoneNumber) {
        this.fullName = fullName;
        this.phoneNumber = phoneNumber;
    }

    public void validate() {
        if (!new FullNameValidator().isValid(fullName))
            throw new RegistrationException("Invalid full name");
        if (!new PhoneNumberValidator().isValid(phoneNumber))
            throw new RegistrationException("Invalid phone number");
    }

    public void dispatch(IUserVisitor visitor, String identifier) {
        visitor.visitIdentity(identifier, fullName);
        visitor.visitPhone(phoneNumber);
    }
}