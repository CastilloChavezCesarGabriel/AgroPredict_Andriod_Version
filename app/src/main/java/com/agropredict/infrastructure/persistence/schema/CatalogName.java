package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.ICatalogRepository;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.repository.SqliteCatalog;

public enum CatalogName {
    SOIL_TYPE("soil_type"),
    PHENOLOGICAL_STAGE("phenological_stage"),
    OCCUPATION("occupation"),
    PROBLEM_TYPE("catalog_problem_type");

    private final String table;

    CatalogName(String table) {
        this.table = table;
    }

    public void create(SQLiteDatabase database) {
        new CatalogTable(table).create(database);
    }

    public void populate(SQLiteDatabase database, String[] entries) {
        new Seed(table, entries).load(database);
    }

    public ICatalogRepository open(Database database) {
        return new SqliteCatalog(database, table);
    }
}