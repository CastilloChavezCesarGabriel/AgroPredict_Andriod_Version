package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class Schema {
    public void create(SQLiteDatabase database) {
        for (CatalogName catalog : CatalogName.values()) {
            catalog.create(database);
        }
        new UserTable().create(database);
        new CropTable().create(database);
        new ImageTable().create(database);
        new QuestionnaireTable().create(database);
        new DiagnosticTable().create(database);
        new ReportTable().create(database);
        new SupportTable().create(database);
        new DiagnosticSummaryView().create(database);
    }
}