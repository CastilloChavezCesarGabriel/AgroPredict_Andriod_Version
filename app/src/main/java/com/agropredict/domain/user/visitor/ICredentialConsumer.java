package com.agropredict.domain.user.visitor;

public interface ICredentialConsumer {
    void authenticate(String email, String password);
}