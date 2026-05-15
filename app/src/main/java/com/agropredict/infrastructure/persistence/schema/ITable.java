package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public interface ITable {
    void create(SQLiteDatabase database);
    void drop(SQLiteDatabase database);
}