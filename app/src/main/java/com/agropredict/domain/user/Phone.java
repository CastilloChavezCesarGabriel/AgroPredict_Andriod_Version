package com.agropredict.domain.user;

import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.user.visitor.IPhoneConsumer;

public final class Phone implements IPhone {
    private final String number;

    public Phone(String number) {
        this.number = ArgumentPrecondition.validate(number, "phone number");
    }

    public static IPhone resolve(String number) {
        return number == null || number.isBlank() ? new NoPhone() : new Phone(number);
    }

    @Override
    public void contact(IPhoneConsumer consumer) {
        consumer.contact(number);
    }
}
