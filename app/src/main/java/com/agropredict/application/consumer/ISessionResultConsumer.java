package com.agropredict.application.consumer;

public interface ISessionResultConsumer {
    void visit(boolean active, String userIdentifier);
}