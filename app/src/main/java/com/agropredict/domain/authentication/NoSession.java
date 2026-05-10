package com.agropredict.domain.authentication;

import com.agropredict.domain.user.IOccupationListener;

public final class NoSession implements ISession {
    @Override
    public void report(ISessionConsumer consumer) {}

    @Override
    public void observe(IOccupationListener listener) {}
}