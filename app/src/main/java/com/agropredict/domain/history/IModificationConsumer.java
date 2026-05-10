package com.agropredict.domain.history;

public interface IModificationConsumer {
    void inscribe(String field);
}