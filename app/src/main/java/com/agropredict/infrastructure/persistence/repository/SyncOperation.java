package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.infrastructure.persistence.database.SqliteRow;

public enum SyncOperation {
    INSERT, UPDATE, DELETE;

    public void record(SqliteRow row) {
        row.record("operation", name());
    }
}
