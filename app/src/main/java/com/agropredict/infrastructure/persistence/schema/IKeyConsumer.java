package com.agropredict.infrastructure.persistence.schema;

public interface IKeyConsumer {
    void accept(String key, String value);
}