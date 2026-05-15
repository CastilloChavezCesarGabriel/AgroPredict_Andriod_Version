package com.agropredict.application.authentication.request;

import com.agropredict.application.factory.failure.IFullNameFailureFactory;
import com.agropredict.application.factory.failure.IPhoneNumberFailureFactory;
import com.agropredict.application.input_validation.FullNameValidator;
import com.agropredict.domain.input_validation.gate.IValidationGate;
import com.agropredict.application.input_validation.PhoneNumberValidator;
import java.util.Objects;

public final class RegistrantFailureContext {
    private final IFullNameFailureFactory fullNameFactory;
    private final IPhoneNumberFailureFactory phoneNumberFactory;

    public RegistrantFailureContext(IFullNameFailureFactory fullNameFactory, IPhoneNumberFailureFactory phoneNumberFactory) {
        this.fullNameFactory = Objects.requireNonNull(fullNameFactory, "registrant failure context requires a full name factory");
        this.phoneNumberFactory = Objects.requireNonNull(phoneNumberFactory, "registrant failure context requires a phone number factory");
    }

    public void check(String fullName, String phoneNumber, IValidationGate gate) {
        new FullNameValidator(fullNameFactory).check(fullName, gate);
        new PhoneNumberValidator(phoneNumberFactory).check(phoneNumber, gate);
    }
}
