package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.repository.SqliteCatalog;

public enum CatalogName implements ITable {
    SOIL_TYPE("soil_type"),
    PHENOLOGICAL_STAGE("phenological_stage"),
    OCCUPATION("occupation"),
    PROBLEM_TYPE("catalog_problem_type");

    private final String table;
    private final CatalogTable schema;

    CatalogName(String table) {
        this.table = table;
        this.schema = new CatalogTable(table);
    }

    @Override
    public void create(SQLiteDatabase database) {
        schema.create(database);
    }

    @Override
    public void drop(SQLiteDatabase database) {
        schema.drop(database);
    }

    public void populate(SQLiteDatabase database, String[] entries) {
        new Seed(table, entries).load(database);
    }

    public ICatalogRepository open(Database database) {
        return new SqliteCatalog(database, table);
    }
}
