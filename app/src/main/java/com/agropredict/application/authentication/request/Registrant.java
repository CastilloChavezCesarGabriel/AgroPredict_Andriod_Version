package com.agropredict.application.authentication.request;

import com.agropredict.domain.input_validation.gate.ValidationGate;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import java.util.Objects;

public final class Registrant {
    private final String fullName;
    private final String phoneNumber;
    private final RegistrantFailureContext failureContext;

    public Registrant(String fullName, String phoneNumber, RegistrantFailureContext failureContext) {
        this.fullName = Objects.requireNonNull(fullName, "registrant requires a full name");
        this.phoneNumber = phoneNumber == null ? "" : phoneNumber;
        this.failureContext = Objects.requireNonNull(failureContext, "registrant requires a failure context");
    }

    public void validate() {
        failureContext.check(fullName, phoneNumber, new ValidationGate());
    }

    public void describe(IUserIdentityConsumer consumer, String identifier) {
        consumer.describe(identifier, fullName);
    }

    public void contact(IPhoneConsumer consumer) {
        consumer.contact(phoneNumber);
    }
}
