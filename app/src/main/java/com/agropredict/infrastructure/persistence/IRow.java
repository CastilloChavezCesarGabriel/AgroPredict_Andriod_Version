package com.agropredict.infrastructure.persistence;

public interface IRow {
    void record(String column, String value);
}