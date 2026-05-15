package com.agropredict.domain.authentication.attempt;

import com.agropredict.domain.authentication.gate.LoginGate;

public interface ILoginAttempt {
    void evaluate(long currentTime, LoginGate gate);
    ILoginAttempt fail(long currentTime);
    ILoginAttempt succeed();
}
