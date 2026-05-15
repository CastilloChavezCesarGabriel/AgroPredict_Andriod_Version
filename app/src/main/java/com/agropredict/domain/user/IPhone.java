package com.agropredict.domain.user;

import com.agropredict.domain.user.visitor.IPhoneConsumer;

public interface IPhone {
    void contact(IPhoneConsumer consumer);
}