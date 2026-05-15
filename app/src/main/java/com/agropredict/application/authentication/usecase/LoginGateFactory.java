package com.agropredict.application.authentication.usecase;

import com.agropredict.application.factory.failure.IAuthenticationFailureFactory;
import com.agropredict.domain.authentication.gate.LoginGate;
import java.util.Objects;

public final class LoginGateFactory {
    private final IAuthenticationFailureFactory failureFactory;

    public LoginGateFactory(IAuthenticationFailureFactory failureFactory) {
        this.failureFactory = Objects.requireNonNull(failureFactory, "login gate factory requires a failure factory");
    }

    public LoginGate create() {
        return new LoginGate(failureFactory.createLockedAccount(), failureFactory.createExhaustedAttempt());
    }
}
