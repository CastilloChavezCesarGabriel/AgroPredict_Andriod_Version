package com.agropredict.domain.authentication;

public interface ILoginGate {
    void allow();
    void block();
    void exhaust();
}