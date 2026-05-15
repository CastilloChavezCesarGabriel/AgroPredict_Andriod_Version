package com.agropredict.domain.authentication.gate;

public final class LoginRejectedException extends RuntimeException {
    public LoginRejectedException(String reason) {
        super(reason);
    }
}