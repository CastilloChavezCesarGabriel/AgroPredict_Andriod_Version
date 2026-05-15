package com.agropredict.domain.authentication.session;

import com.agropredict.domain.user.occupation.IOccupationListener;

public final class NoSession implements ISession {
    @Override
    public void report(ISessionConsumer consumer) {}

    @Override
    public void observe(IOccupationListener listener) {}
}