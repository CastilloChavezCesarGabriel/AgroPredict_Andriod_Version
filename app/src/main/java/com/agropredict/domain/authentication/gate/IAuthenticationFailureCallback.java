package com.agropredict.domain.authentication.gate;

public interface IAuthenticationFailureCallback {
    void receive(String text);
}
