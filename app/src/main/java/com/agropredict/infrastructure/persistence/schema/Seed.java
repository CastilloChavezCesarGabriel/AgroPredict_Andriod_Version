package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class Seed {
    private final String table;
    private final String[] values;

    public Seed(String table, String[] values) {
        this.table = table;
        this.values = values;
    }

    public void load(SQLiteDatabase database) {
        for (String value : values) {
            database.execSQL(
                "INSERT OR IGNORE INTO " + table + " (name) VALUES (?)",
                new Object[]{value});
        }
    }
}