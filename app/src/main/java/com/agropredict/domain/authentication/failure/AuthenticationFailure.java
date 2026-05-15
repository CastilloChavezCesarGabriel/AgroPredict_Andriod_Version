package com.agropredict.domain.authentication.failure;

import com.agropredict.domain.authentication.gate.IAuthenticationFailureCallback;
import com.agropredict.domain.authentication.gate.IAuthenticationFailurePhrase;
import java.util.Objects;

public final class AuthenticationFailure implements IAuthenticationFailure {
    private final IAuthenticationFailurePhrase phrase;

    public AuthenticationFailure(IAuthenticationFailurePhrase phrase) {
        this.phrase = Objects.requireNonNull(phrase, "authentication failure requires a phrase");
    }

    @Override
    public void encode(IAuthenticationFailureCallback callback) {
        callback.receive(phrase.describe());
    }
}
