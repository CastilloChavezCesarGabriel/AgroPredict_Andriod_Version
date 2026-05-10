package com.agropredict.domain.user;

import com.agropredict.domain.authentication.ISessionConsumer;

public abstract class Occupation {
    protected final String value;

    protected Occupation(String value) {
        this.value = value;
    }

    public abstract void accept(IOccupationListener listener);

    public final void report(ISessionConsumer consumer, String userIdentifier) {
        consumer.report(userIdentifier, value);
    }
}