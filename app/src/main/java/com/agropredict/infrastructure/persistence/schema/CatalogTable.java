package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.domain.guard.ArgumentPrecondition;

public final class CatalogTable implements ITable {
    private final String name;

    public CatalogTable(String name) {
        this.name = ArgumentPrecondition.validate(name, "catalog table name");
    }

    @Override
    public void create(SQLiteDatabase database) {
        database.execSQL(
            "CREATE TABLE IF NOT EXISTS " + name + " ("
            + "id TEXT PRIMARY KEY, "
            + "name TEXT NOT NULL UNIQUE)");
    }
}
