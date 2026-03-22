package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class CatalogTable implements ITable {
    private final String name;

    public CatalogTable(String name) {
        this.name = name;
    }

    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS " + name + " ("
            + "id TEXT PRIMARY KEY DEFAULT (lower(hex(randomblob(16)))), "
            + "name TEXT NOT NULL UNIQUE)");
    }
}