package com.agropredict.application.authentication.request;

import com.agropredict.domain.input_validation.FullNameValidator;
import com.agropredict.domain.input_validation.PhoneNumberValidator;
import com.agropredict.domain.input_validation.ValidationGate;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import java.util.Objects;

public final class Registrant {
    private final String fullName;
    private final String phoneNumber;

    public Registrant(String fullName, String phoneNumber) {
        this.fullName = Objects.requireNonNull(fullName, "registrant requires a full name");
        this.phoneNumber = Objects.requireNonNull(phoneNumber, "registrant requires a phone number");
    }

    public void validate() {
        ValidationGate gate = new ValidationGate();
        new FullNameValidator().check(fullName, gate);
        new PhoneNumberValidator().check(phoneNumber, gate);
    }

    public void describe(IUserIdentityConsumer consumer, String identifier) {
        consumer.describe(identifier, fullName);
    }

    public void contact(IPhoneConsumer consumer) {
        consumer.contact(phoneNumber);
    }
}
