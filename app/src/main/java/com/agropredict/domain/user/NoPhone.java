package com.agropredict.domain.user;

import com.agropredict.domain.user.visitor.IPhoneConsumer;

public final class NoPhone implements IPhone {
    @Override
    public void contact(IPhoneConsumer consumer) {}
}