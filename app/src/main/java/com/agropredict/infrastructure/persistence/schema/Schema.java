package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class Schema {
    public void create(SQLiteDatabase database) {
        new CatalogTable("occupation").create(database);
        new CatalogTable("soil_type").create(database);
        new CatalogTable("phenological_stage").create(database);
        new CatalogTable("catalog_problem_type").create(database);
        new UserTable().create(database);
        new CropTable().create(database);
        new ImageTable().create(database);
        new DiagnosticTable().create(database);
        new QuestionnaireTable().create(database);
        new ReportTable().create(database);
        new SupportTable().create(database);
        new DiagnosticSummaryView().create(database);
    }
}