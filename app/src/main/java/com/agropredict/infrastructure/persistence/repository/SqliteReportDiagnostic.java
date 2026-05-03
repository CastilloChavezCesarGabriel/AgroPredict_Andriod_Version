package com.agropredict.infrastructure.persistence.repository;

import android.content.ContentValues;
import com.agropredict.infrastructure.persistence.database.Database;

public final class SqliteReportDiagnostic {
    private final Database database;

    public SqliteReportDiagnostic(Database database) {
        this.database = database;
    }

    public void link(String reportIdentifier, String diagnosticIdentifier) {
        ContentValues values = new ContentValues();
        values.put("report_id", reportIdentifier);
        values.put("diagnostic_id", diagnosticIdentifier);
        values.put("position", 1);
        values.put("observations", "Generated report");
        database.getWritableDatabase().insert("report_diagnostic", null, values);
    }
}
