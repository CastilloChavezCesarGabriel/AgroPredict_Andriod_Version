package com.agropredict.infrastructure.persistence.database;

public interface IRow {
    void record(String column, String value);
}