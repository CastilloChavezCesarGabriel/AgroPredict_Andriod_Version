package com.agropredict.infrastructure.persistence;

public interface IRecord {
    void record(String column, String value);
}
