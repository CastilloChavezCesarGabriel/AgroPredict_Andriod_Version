package com.agropredict.domain.crop.visitor;

public interface ICropIdentityConsumer {
    void describe(String identifier, String cropType);
}