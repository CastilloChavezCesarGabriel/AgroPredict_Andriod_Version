package com.agropredict.domain.authentication;

public final class LoginRejectedException extends RuntimeException {
    public LoginRejectedException(String reason) {
        super(reason);
    }
}
