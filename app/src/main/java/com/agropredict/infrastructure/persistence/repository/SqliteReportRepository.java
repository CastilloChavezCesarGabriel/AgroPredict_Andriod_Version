package com.agropredict.infrastructure.persistence.repository;

import com.agropredict.application.repository.IReportRepository;
import com.agropredict.application.request.report_generation.ReportRequest;
import com.agropredict.application.request.report_generation.Destination;
import com.agropredict.infrastructure.persistence.database.Database;
import com.agropredict.infrastructure.persistence.database.SqliteRow;
import com.agropredict.infrastructure.persistence.visitor.ReportPersistenceVisitor;

public final class SqliteReportRepository implements IReportRepository {
    private final Database database;

    public SqliteReportRepository(Database database) {
        this.database = database;
    }

    @Override
    public void store(ReportRequest request, Destination destination) {
        SqliteRow row = new SqliteRow(database.getWritableDatabase());
        request.accept(new ReportPersistenceVisitor(row), destination);
        row.flush("report");
    }
}
