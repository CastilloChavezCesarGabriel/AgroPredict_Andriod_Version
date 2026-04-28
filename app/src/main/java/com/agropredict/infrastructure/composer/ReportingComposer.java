package com.agropredict.infrastructure.composer;

import android.content.Context;
import com.agropredict.application.repository.ICropRepository;
import com.agropredict.application.repository.IDiagnosticRepository;
import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.repository.ISessionRepository;
import com.agropredict.application.service.IReportService;
import com.agropredict.application.factory.IReportingFactory;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.repository.SessionRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteCropRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteDiagnosticRepository;
import com.agropredict.infrastructure.persistence.repository.SqliteReportRepository;
import com.agropredict.infrastructure.report_export.CsvReportService;
import com.agropredict.infrastructure.report_export.PdfReportService;
import java.io.File;

public final class ReportingComposer implements IReportingFactory {
    private final Database database;
    private final Context context;

    public ReportingComposer(Database database, Context context) {
        this.database = database;
        this.context = context;
    }

    @Override
    public ICropRepository createCropRepository() {
        return new SqliteCropRepository(database, createSessionRepository());
    }

    @Override
    public IDiagnosticRepository createDiagnosticRepository() {
        return new SqliteDiagnosticRepository(database);
    }

    @Override
    public IReportRepository createReportRepository() {
        return new SqliteReportRepository(database);
    }

    @Override
    public IReportService createReportService(String format) {
        File reports = new File(context.getExternalFilesDir(null), "reports");
        if (!reports.exists() && !reports.mkdirs()) {
            throw new RuntimeException("Cannot create reports directory");
        }
        return "csv".equals(format)
                ? new CsvReportService(reports)
                : new PdfReportService(reports);
    }

    @Override
    public ISessionRepository createSessionRepository() {
        return new SessionRepository(context);
    }
}