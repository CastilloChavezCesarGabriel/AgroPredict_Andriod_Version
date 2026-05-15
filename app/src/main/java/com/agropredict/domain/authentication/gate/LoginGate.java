package com.agropredict.domain.authentication.gate;

import com.agropredict.domain.authentication.failure.IAuthenticationFailure;
import java.util.Objects;

public final class LoginGate {
    private final IAuthenticationFailure blockFailure;
    private final IAuthenticationFailure exhaustFailure;

    public LoginGate(IAuthenticationFailure blockFailure, IAuthenticationFailure exhaustFailure) {
        this.blockFailure = Objects.requireNonNull(blockFailure, "login gate requires a block failure");
        this.exhaustFailure = Objects.requireNonNull(exhaustFailure, "login gate requires an exhaust failure");
    }

    public void allow() {}

    public void block() {
        blockFailure.encode(text -> {
            throw new LoginRejectedException(text);
        });
    }

    public void exhaust() {
        exhaustFailure.encode(text -> {
            throw new LoginRejectedException(text);
        });
    }
}