package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.application.request.report_generation.Destination;
import com.agropredict.infrastructure.persistence.database.Clock;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.ReportPersistenceVisitor;

public final class SqliteReportRepository implements IReportRepository {
    private final Database database;
    private final SqliteReportDiagnostic reportDiagnostic;
    private final SqliteReportSharing reportSharing;

    public SqliteReportRepository(Database database) {
        this.database = database;
        this.reportDiagnostic = new SqliteReportDiagnostic(database);
        this.reportSharing = new SqliteReportSharing(database);
    }

    @Override
    public void store(ReportRequest request, Destination destination) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        request.accept(new ReportPersistenceVisitor(row), destination);
        String now = Clock.read();
        row.record("generated_at", now);
        row.flush("report");
        request.identify((reportIdentifier, diagnosticIdentifier) -> link(reportIdentifier, diagnosticIdentifier, now));
    }

    private void link(String reportIdentifier, String diagnosticIdentifier, String generatedAt) {
        if (diagnosticIdentifier != null) reportDiagnostic.link(reportIdentifier, diagnosticIdentifier);
        reportSharing.share(reportIdentifier, generatedAt);
    }

    @Override
    public void clear(String cropIdentifier) {
        database.getWritableDatabase().delete("report", "crop_id = ?", new String[]{cropIdentifier});
    }
}
