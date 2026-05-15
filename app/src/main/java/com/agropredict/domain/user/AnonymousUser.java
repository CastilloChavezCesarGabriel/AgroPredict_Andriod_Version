package com.agropredict.domain.user;

import com.agropredict.domain.authentication.session.ISessionBuilder;

public final class AnonymousUser implements ISessionSubject {
    @Override
    public boolean expose(ISessionBuilder builder) {
        return false;
    }
}