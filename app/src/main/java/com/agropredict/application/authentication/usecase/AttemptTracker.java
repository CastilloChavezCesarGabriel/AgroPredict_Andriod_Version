package com.agropredict.application.authentication.usecase;

import com.agropredict.domain.authentication.attempt.ILoginAttempt;
import com.agropredict.domain.authentication.attempt.InitialAttempt;
import com.agropredict.domain.authentication.gate.LoginGate;

public final class AttemptTracker {
    private ILoginAttempt attempt;

    public AttemptTracker() {
        this.attempt = new InitialAttempt();
    }

    public void evaluate(long currentTime, LoginGate gate) {
        attempt.evaluate(currentTime, gate);
    }

    public void succeed() {
        attempt = attempt.succeed();
    }

    public void fail(long currentTime) {
        attempt = attempt.fail(currentTime);
    }
}
