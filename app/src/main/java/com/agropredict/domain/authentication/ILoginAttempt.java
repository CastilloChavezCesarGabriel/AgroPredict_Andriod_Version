package com.agropredict.domain.authentication;

public interface ILoginAttempt {
    void evaluate(long currentTime, ILoginGate gate);
    ILoginAttempt fail(long currentTime);
    ILoginAttempt succeed();
}