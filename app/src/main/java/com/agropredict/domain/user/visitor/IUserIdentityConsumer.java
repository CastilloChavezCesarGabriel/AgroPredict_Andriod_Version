package com.agropredict.domain.user.visitor;

public interface IUserIdentityConsumer {
    void describe(String identifier, String fullName);
}