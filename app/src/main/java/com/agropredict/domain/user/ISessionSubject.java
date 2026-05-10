package com.agropredict.domain.user;

import com.agropredict.domain.authentication.ISessionBuilder;

public interface ISessionSubject {
    boolean expose(ISessionBuilder builder);
}