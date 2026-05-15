package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.report_generation.request.ReportRequest;
import com.agropredict.application.report_generation.request.Destination;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.database.SqliteRowFactory;
import com.agropredict.infrastructure.persistence.visitor.ReportPersistenceVisitor;
import java.util.Objects;

public final class SqliteReportRepository implements IReportRepository {
    private final SqliteReportDiagnostic reportDiagnostic;
    private final SqliteReportSharing reportSharing;
    private final SqliteRowFactory rowFactory;

    public SqliteReportRepository(Database database, SqliteRowFactory rowFactory) {
        Objects.requireNonNull(database, "report repository requires a database");
        this.rowFactory = Objects.requireNonNull(rowFactory, "report repository requires a row factory");
        this.reportDiagnostic = new SqliteReportDiagnostic(database);
        this.reportSharing = new SqliteReportSharing(rowFactory);
    }

    @Override
    public void store(ReportRequest request, Destination destination) {
        SqliteRow row = rowFactory.open();
        ReportPersistenceVisitor visitor = new ReportPersistenceVisitor(row);
        request.describe(visitor);
        request.link(visitor);
        request.store(visitor, destination);
        row.stamp("generated_at");
        row.mark("is_active", 1);
        row.flush("report");
        request.identify(this::link);
    }

    private void link(String reportIdentifier, String diagnosticIdentifier) {
        if (diagnosticIdentifier != null) reportDiagnostic.link(reportIdentifier, diagnosticIdentifier);
        reportSharing.share(reportIdentifier);
    }
}
