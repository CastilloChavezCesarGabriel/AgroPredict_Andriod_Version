package com.agropredict.domain.authentication;

import com.agropredict.domain.user.IOccupationListener;

public interface ISession {
    void report(ISessionConsumer consumer);
    void observe(IOccupationListener listener);
}
