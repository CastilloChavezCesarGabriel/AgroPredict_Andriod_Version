package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.domain.Identifier;

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
                    "INSERT OR IGNORE INTO " + table + " (id, name) VALUES (?, ?)",
                    new Object[]{Identifier.generate(table), value});
        }
    }
}