package com.agropredict.domain.authentication.session;

public interface ISessionConsumer {
    void report(String userIdentifier, String occupation);
}
