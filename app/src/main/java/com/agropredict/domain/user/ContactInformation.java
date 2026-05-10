package com.agropredict.domain.user;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;

public final class ContactInformation {
    private final String fullName;
    private final String phoneNumber;

    public ContactInformation(String fullName, String phoneNumber) {
        this.fullName = ArgumentPrecondition.validate(fullName, "contact full name");
        this.phoneNumber = phoneNumber;
    }

    public void describe(IUserIdentityConsumer consumer, String identifier) {
        consumer.describe(identifier, fullName);
    }

    public void contact(IPhoneConsumer consumer) {
        if (phoneNumber != null) consumer.contact(phoneNumber);
    }
}