package com.agropredict.infrastructure.persistence;

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
        Recorder recorder = record(report);
        recorder.flush("report");
    }

    private Recorder record(Report report) {
        Recorder recorder = new Recorder(databaseHelper.getWritableDatabase());
        report.accept(new ReportRecorder(recorder));
        return recorder;
    }
}