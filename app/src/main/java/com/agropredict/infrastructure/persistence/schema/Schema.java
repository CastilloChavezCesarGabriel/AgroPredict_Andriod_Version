package com.agropredict.infrastructure.persistence.schema;

import android.database.sqlite.SQLiteDatabase;

public final class Schema {
    private static final String[] DROP_TABLES = {
        "ai_user_response", "ai_option", "ai_question",
        "report_sharing", "report_diagnostic", "report",
        "diagnostic", "image", "crop_history", "crop",
        "log_entry", "sync_pending", "user",
        "occupation", "soil_type", "phenological_stage", "catalog_problem_type"
    };

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
    }

    public void drop(SQLiteDatabase database) {
        for (String table : DROP_TABLES) {
            database.execSQL("DROP TABLE IF EXISTS " + table);
        }
    }
}