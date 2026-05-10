package com.agropredict.domain.authentication;

public final class LoginGate implements ILoginGate {
    @Override
    public void allow() {}

    @Override
    public void block() {
        throw new LoginRejectedException("Account locked. Try again in a few minutes.");
    }

    @Override
    public void exhaust() {
        throw new LoginRejectedException("Too many attempts. Account locked for 5 minutes.");
    }
}
