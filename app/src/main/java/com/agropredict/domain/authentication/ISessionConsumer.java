package com.agropredict.domain.authentication;

public interface ISessionConsumer {
    void report(String userIdentifier, String occupation);
}
