package com.agropredict.domain.user;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.user.visitor.IPhoneConsumer;
import com.agropredict.domain.user.visitor.IUserIdentityConsumer;
import java.util.Objects;

public final class ContactInformation {
    private final String fullName;
    private final IPhone phone;

    public ContactInformation(String fullName, IPhone phone) {
        this.fullName = ArgumentPrecondition.validate(fullName, "contact full name");
        this.phone = Objects.requireNonNull(phone, "contact information requires a phone");
    }

    public void describe(IUserIdentityConsumer consumer, String identifier) {
        consumer.describe(identifier, fullName);
    }

    public void contact(IPhoneConsumer consumer) {
        phone.contact(consumer);
    }
}
