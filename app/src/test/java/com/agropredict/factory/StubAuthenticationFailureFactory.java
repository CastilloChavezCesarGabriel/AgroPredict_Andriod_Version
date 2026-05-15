package com.agropredict.factory;

import com.agropredict.application.factory.failure.IAuthenticationFailureFactory;
import com.agropredict.domain.authentication.failure.IAuthenticationFailure;

public final class StubAuthenticationFailureFactory implements IAuthenticationFailureFactory {
    @Override
    public IAuthenticationFailure createIncorrectCredential() {
        return callback -> callback.receive("Incorrect credentials");
    }

    @Override
    public IAuthenticationFailure createLockedAccount() {
        return callback -> callback.receive("Account locked. Try again in a few minutes.");
    }

    @Override
    public IAuthenticationFailure createExhaustedAttempt() {
        return callback -> callback.receive("Too many failed attempts. Please try again later.");
    }
}
