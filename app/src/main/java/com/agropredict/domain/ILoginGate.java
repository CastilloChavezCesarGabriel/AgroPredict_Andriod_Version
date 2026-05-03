package com.agropredict.domain;

public interface ILoginGate {
    void allow();
    void block();
    void exhaust();
}