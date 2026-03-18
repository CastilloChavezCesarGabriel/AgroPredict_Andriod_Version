package com.agropredict.application.consumer;

public interface IRegistrationResultConsumer {
    void visit(boolean completed, String errorMessage);
}