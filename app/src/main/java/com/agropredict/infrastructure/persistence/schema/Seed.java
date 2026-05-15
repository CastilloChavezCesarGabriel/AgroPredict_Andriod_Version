package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.domain.guard.ArgumentPrecondition;
import com.agropredict.domain.identifier.IdentifierFactory;
import java.util.Objects;

public final class Seed {
    private final String table;
    private final String[] values;

    public Seed(String table, String[] values) {
        this.table = ArgumentPrecondition.validate(table, "seed table");
        this.values = Objects.requireNonNull(values, "seed requires values");
    }

    public void load(SQLiteDatabase database) {
        for (String value : values) {
            database.execSQL(
                    "INSERT OR IGNORE INTO " + table + " (id, name) VALUES (?, ?)",
                    new Object[]{IdentifierFactory.generate(table), value});
        }
    }
}