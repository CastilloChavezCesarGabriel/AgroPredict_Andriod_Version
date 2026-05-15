package com.agropredict.domain.authentication.session;

import com.agropredict.domain.user.occupation.IOccupationListener;

public interface ISession {
    void report(ISessionConsumer consumer);
    void observe(IOccupationListener listener);
}
