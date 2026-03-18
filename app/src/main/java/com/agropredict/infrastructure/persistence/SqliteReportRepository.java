package com.agropredict.infrastructure.persistence;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.domain.entity.Report;

public final class SqliteReportRepository implements IReportRepository {
    private final DatabaseHelper databaseHelper;

    public SqliteReportRepository(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }

    @Override
    public void store(Report report) {
        SQLiteDatabase database = databaseHelper.getWritableDatabase();
        ContentValues values = record(report);
        database.insert("report", null, values);
    }

    private ContentValues record(Report report) {
        ContentValues values = new ContentValues();
        report.accept(new ReportRecorder(values));
        return values;
    }
}
